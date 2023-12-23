package com.plumekanade.mark.data.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.mark.data.entity.*;
import com.plumekanade.mark.data.enums.CompanyRosterSourceEnum;
import com.plumekanade.mark.data.service.*;
import com.plumekanade.mark.data.vo.ResultMsg;
import com.plumekanade.mark.data.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.*;

/**
 * 企业名单数据导入类
 * @author kanade
 * @date 2023-12-15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInit implements ApplicationRunner {
    @Value("${subPath}")
    private String subPath;
    private final ThreadPoolTaskExecutor threadTaskExecutor;
    private final CompanyRosterService companyRosterService;
    private final CompanyRosterSourceService companyRosterSourceService;
    private final CompanyRosterContactService companyRosterContactService;
    private final BfCompanyRosterService bfCompanyRosterService;
    private final MarkerCompanyDataService markerCompanyDataService;
    private final MarkerCompanyOffDataService markerCompanyOffDataService;
    private final List<String> shortKeys = new ArrayList<>(Arrays.asList("companyName", "manageState", "legalName", "registerCapital", "realCapital", "establishDate", "verifyDate", "businessTerm", "province", "city", "district", "creditCode", "taxpayerCode", "businessRegisterCode", "organizationCode", "insureNum", "companyType", "industry", "previousName", "registerAddress", "url", "contactPhone", "email", "businessScope"));
    private final List<String> longKeys = new ArrayList<>(Arrays.asList("companyName", "engName", "creditCode", "companyType", "manageState", "establishDate", "verifyDate", "legalName", "registerCapital", "realCapital", "insureNum", "companySize", "businessScope", "registerAddress", "businessTerm", "source", "taxpayerCode", "businessRegisterCode", "organizationCode", "contactPhone", "email", "taxpayerCredential", "previousName", "province", "city", "district", "url", "industry", "firstClassify", "secondClassify", "thirdClassify", "registrar", "lon", "lat"));

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 八方数据合并到名单库
        threadTaskExecutor.submit(() -> {
            int current = 1;
            int size = 1000;
            boolean flag = true;
            while (flag) {
                Page<BfCompanyRoster> page = bfCompanyRosterService.page(new Page<>(current++, size));
                flag = page.hasNext();
                threadTaskExecutor.submit(() -> handleMergeBf(page.getRecords()));
                if (!flag) {
                    break;
                }
                Page<BfCompanyRoster> page1 = bfCompanyRosterService.page(new Page<>(current++, size));
                flag = page1.hasNext();
                threadTaskExecutor.submit(() -> handleMergeBf(page1.getRecords()));
                if (!flag) {
                    break;
                }
                Page<BfCompanyRoster> page2 = bfCompanyRosterService.page(new Page<>(current++, size));
                threadTaskExecutor.submit(() -> handleMergeBf(page2.getRecords()));
                flag = page2.hasNext();
                if (!flag) {
                    break;
                }
                Page<BfCompanyRoster> page3 = bfCompanyRosterService.page(new Page<>(current++, size));
                threadTaskExecutor.submit(() -> handleMergeBf(page3.getRecords()));
                flag = page3.hasNext();
                if (!flag) {
                    break;
                }
                Page<BfCompanyRoster> page4 = bfCompanyRosterService.page(new Page<>(current++, size));
                handleMergeBf(page4.getRecords());
                flag = page4.hasNext();
            }
        });



        // 马克数据导入
        threadTaskExecutor.submit(() -> {
            // 导入成功后的文件目录
            String moveDir = "C:\\Users\\equipl\\code\\ImportSuccess\\";
            // 读取数据文件目录
            File[] files = FileUtil.ls("C:\\Users\\equipl\\code\\全国数据\\" + subPath + "\\");
            String off = "注销";
            String offed = "已注销";
            for (File file : files) {
                List<MarkerCompanyData> list = new ArrayList<>();
                List<MarkerCompanyOffData> offList = new ArrayList<>();
                // 报错后不再插入, 等待重新修改再插入 随意找个对象, 不能boolean值判断（Lambda）
                ResultMsg resultMsg = ResultMsg.success();
                ExcelUtil.readBySax(file, 0, (sheetIndex, rowIndex, rowList) -> {
                    // 跳过字段定义行以及可能存在的广告行（最后一行）
                    if (rowIndex == 0) {
                        resultMsg.setShortFlag(rowList.size() < 30);     // true -> 少字段文档  false -> 多字段文档
                    }
                    if (rowIndex == 0 || "更多数据，尽在“马克数据网”".equals(rowList.get(0)) || resultMsg.getCode() != 0) {
                        log.error("【名单数据】跳过数据行: {}", rowIndex);
                        return;
                    }
                    try {
                        Map<String, Object> map = new HashMap<>(rowList.size());
                        for (int j = 0; j < rowList.size(); j++) {
                            if (j >= shortKeys.size() || j >= longKeys.size()) {
                                break;
                            }
                            map.put(resultMsg.isShortFlag() ? shortKeys.get(j) : longKeys.get(j), rowList.get(j));
                        }
                        String data = MapperUtils.serialize(map);
                        // 判断是否注销数据
                        MarkerCompanyData dataItem = null;
                        MarkerCompanyOffData offDataItem = null;
                        String manageState = map.get("manageState").toString();
                        if (off.equals(manageState) || manageState.contains(offed)) {
                            offDataItem = MapperUtils.deserialize(data, MarkerCompanyOffData.class);
                        } else {
                            dataItem = MapperUtils.deserialize(data, MarkerCompanyData.class);
                        }
                        if (offDataItem != null) {
                            offList.add(offDataItem);
                            if (offList.size() >= 100) {
                                try {
                                    log.info("【注销数据】文档 {} - {} 触发批量插入数据, 结果：{}", file.getName(), rowIndex, markerCompanyOffDataService.saveBatch(offList));
                                } catch (DuplicateKeyException e) {
                                    handleOffDuplicateKey(offList);
                                    log.error("【注销数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                                }
                                offList.clear();
                            }
                        } else if (dataItem != null) {
                            list.add(dataItem);
                            if (list.size() >= 100) {
                                try {
                                    log.info("【名单数据】文档 {} - {} 触发批量插入数据, 结果：{}", file.getName(), rowIndex, markerCompanyDataService.saveBatch(list));
                                } catch (DuplicateKeyException e) {
                                    handleDuplicateKey(list);
                                    log.error("【名单数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                                }
                                list.clear();
                            }
                        } else {
                            log.error("【名单数据】跳过文档行: {}, 数据为空", rowIndex);
                        }
                    } catch (Exception e) {
                        resultMsg.setCode(-1);
                        log.error("【名单数据】处理数据异常, 堆栈: ", e);
                    }
                });
                if (resultMsg.getCode() == 0) {
                    if (!list.isEmpty()) {
                        try {
                            log.info("【名单数据】文件 {} 最后一次批量插入数据, 结果：{}", file.getName(), markerCompanyDataService.saveBatch(list));
                        } catch (DuplicateKeyException e) {
                            handleDuplicateKey(list);
                            log.error("【名单数据】文件 {} 最后一次批量插入, 唯一键/主键冲突异常", file.getName());
                        }
                    }
                    if (!offList.isEmpty()) {
                        try {
                            log.info("【注销数据】文件 {} 最后一次批量插入注销数据, 结果：{}", file.getName(), markerCompanyOffDataService.saveBatch(offList));
                        } catch (DuplicateKeyException e) {
                            handleOffDuplicateKey(offList);
                            log.error("【注销数据】文件 {} 最后一次批量插入, 唯一键/主键冲突异常", file.getName());
                        }

                    }
                    try {
                        log.info("【移动数据文件】文件 {} 数据导入完成, 移动到新目录结果: {}", file.getName(), file.renameTo(new File(moveDir + file.getName())));
                    } catch (Exception e) {
                        log.error("【移动数据文件】文件 {} 移动失败, 异常堆栈: ", file.getName(), e);
                    }
                } else {
                    log.error("【名单导入】未能进入文件结束流程, code 值为 -1, ResultMsg: {}", MapperUtils.serialize(resultMsg));
                }
            }

            log.info("【名单导入】文件夹内数据已全部导入...");
        });
    }

    /** 处理八方数据合并到名单库 */
    public void handleMergeBf(List<BfCompanyRoster> bfRosters) {
        List<CompanyRoster> rosters = new ArrayList<>();
        for (BfCompanyRoster roster : bfRosters) {
            CompanyRoster companyRoster = new CompanyRoster(roster.getCompanyName(), roster.getCreditCode(), roster.getManageState(), roster.getCompanyUrl(), roster.getContactPhone(), roster.getCompanyKeyword(), roster.getCompanyAddress(), roster.getRegisterAddress(), roster.getStaffNum(), roster.getBusinessScope());
            companyRoster.setBfProps(roster);
            companyRoster.setContact(new CompanyRosterContact(roster, companyRoster.getLegalName()));
            rosters.add(companyRoster);
        }
        boolean flag = companyRosterService.saveBatch(rosters);
        if (!flag) {
            log.error("【名单库】批量插入到名单库未知失败....");
        }
        threadTaskExecutor.submit(() -> {
           List<CompanyRosterContact> contacts = new ArrayList<>();
           List<CompanyRosterSource> sources = new ArrayList<>();
            try {
                rosters.forEach(roster -> {
                    CompanyRosterContact contact = roster.getContact();
                    contact.setCompanyRosterId(roster.getId());
                    contacts.add(contact);
                    sources.add(new CompanyRosterSource(roster.getId(), CompanyRosterSourceEnum.BF));
                });
                companyRosterContactService.saveBatch(contacts);
                companyRosterSourceService.saveBatch(sources);
            } catch (Exception e) {
                log.error("【联系人&来源】批量插入联系人&来源关联数据失败, 异常堆栈: ", e);
            }
        });
    }

    /** 处理马克数据合并到名单库 */
    public void handleMarkMerge(List<MarkerCompanyData> markRosters) {
        List<CompanyRoster> rosters = new ArrayList<>();
        for (MarkerCompanyData markRoster : markRosters) {
            CompanyRoster companyRoster = new CompanyRoster(markRoster.getCompanyName(), markRoster.getCreditCode(), markRoster.getManageState(), markRoster.getUrl(), markRoster.getContactPhone(), "", "", markRoster.getRegisterAddress(), markRoster.getCompanySize(), markRoster.getBusinessScope());
            companyRoster.setMarkProps(markRoster);
            rosters.add(companyRoster);
        }
        boolean flag = companyRosterService.saveBatch(rosters);
        if (flag) {
            List<CompanyRosterSource> list = new ArrayList<>(rosters.size());
            rosters.forEach(item -> list.add(new CompanyRosterSource(item.getId(), CompanyRosterSourceEnum.MARK)));
            companyRosterSourceService.saveBatch(list);
        } else {
            log.error("【马克数据】批量合并失败, 数据库添加结果为 false, 未出现异常");
        }
    }

    /**
     * 拆分100条/次
     */
    public void handleDuplicateKey(List<MarkerCompanyData> dataList) {
        List<MarkerCompanyData> subList = new ArrayList<>();
        dataList.forEach(data -> {
            subList.add(data);
            if (subList.size() >= 10) {
                threadTaskExecutor.submit(() -> handleSubDuplicateKey(new ArrayList<>(subList)));
                subList.clear();
            }
        });
        if (!subList.isEmpty()) {
            threadTaskExecutor.submit(() -> handleSubDuplicateKey(subList));
        }
    }

    public void handleSubDuplicateKey(List<MarkerCompanyData> subList) {
        try {
            log.info("【数据冲突】文档数据冲突, 小批量插入数据, 结果：{}", markerCompanyDataService.saveBatch(subList));
        } catch (Exception e) {
            List<MarkerCompanyData> updateList = new ArrayList<>();
            subList.forEach(sub -> {
                try {
                    markerCompanyDataService.save(sub);
                } catch (DuplicateKeyException ex) {
                    try {
                        sub.setId(markerCompanyDataService.getOne(Wrappers.lambdaUpdate(MarkerCompanyData.class).eq(MarkerCompanyData::getCompanyName, sub.getCompanyName())).getId());
                        updateList.add(sub);
                    } catch (Exception exc) {
                        log.error("【名单数据】以企业名称 {} 查找对应的数据失败, 堆栈: ", sub.getCompanyName(), exc);
                    }
                } catch (Exception ee) {
                    log.error("【名单数据】单条插入异常, 堆栈: ", ee);
                }
            });
            if (!updateList.isEmpty()) {
                try {
                    markerCompanyDataService.updateBatchById(updateList);
                } catch (Exception ex) {
                    log.error("【名单数据】根据id批量更新失败, 异常堆栈:　", ex);
                }
            }
        }
    }

    /**
     * 拆分100条/次
     */
    public void handleOffDuplicateKey(List<MarkerCompanyOffData> dataList) {
        List<MarkerCompanyOffData> subList = new ArrayList<>();
        dataList.forEach(data -> {
            subList.add(data);
            if (subList.size() >= 10) {
                threadTaskExecutor.submit(() -> handleOffSubDuplicateKey(new ArrayList<>(subList)));
                subList.clear();
            }
        });
        if (!subList.isEmpty()) {
            threadTaskExecutor.submit(() -> handleOffSubDuplicateKey(subList));
        }
    }

    public void handleOffSubDuplicateKey(List<MarkerCompanyOffData> subList) {
        try {
            log.info("【注销数据冲突】文档数据冲突, 小批量插入数据, 结果：{}", markerCompanyOffDataService.saveBatch(subList));
        } catch (Exception e) {
            List<MarkerCompanyOffData> updateList = new ArrayList<>();
            subList.forEach(sub -> {
                try {
                    markerCompanyOffDataService.save(sub);
                } catch (DuplicateKeyException ex) {
                    try {
                        sub.setId(markerCompanyOffDataService.getOne(Wrappers.lambdaUpdate(MarkerCompanyOffData.class).eq(MarkerCompanyOffData::getCompanyName, sub.getCompanyName())).getId());
                        updateList.add(sub);
                    } catch (Exception exc) {
                        log.error("【注销数据】以企业名称 {} 查找对应的数据失败, 堆栈: ", sub.getCompanyName(), exc);
                    }
                } catch (Exception ee) {
                    log.error("【注销数据】单条插入异常, 堆栈: ", ee);
                }
            });
            try {
                markerCompanyOffDataService.updateBatchById(updateList);
            } catch (Exception ex) {
                log.error("【注销数据】根据id批量更新失败, 异常堆栈:　", ex);
            }
        }
    }
}
