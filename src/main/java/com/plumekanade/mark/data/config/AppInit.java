package com.plumekanade.mark.data.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.mark.data.entity.*;
import com.plumekanade.mark.data.enums.CompanyRosterSourceEnum;
import com.plumekanade.mark.data.service.*;
import com.plumekanade.mark.data.vo.ResultMsg;
import com.plumekanade.mark.data.utils.MapperUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.BatchUpdateException;
import java.util.*;

/**
 企业名单数据导入类
 @author kanade */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInit implements ApplicationRunner {
    @Value("${subPath}")
    private String subPath;
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;
    private final CompanyRosterService companyRosterService;
    private final CompanyRosterIndustryService companyRosterIndustryService;
    private final CompanyRosterSourceService companyRosterSourceService;
    private final CompanyRosterContactService companyRosterContactService;
    private final BfCompanyRosterService bfCompanyRosterService;
    private final MarkerCompanyDataService markerCompanyDataService;
    private final MarkerCompanyOffDataService markerCompanyOffDataService;
    private final List<String> shortKeys = new ArrayList<>(Arrays.asList("companyName", "manageState", "legalName", "registerCapital", "realCapital", "establishDate", "verifyDate", "businessTerm", "province", "city", "district", "creditCode", "taxpayerCode", "businessRegisterCode", "organizationCode", "insureNum", "companyType", "industry", "previousName", "registerAddress", "url", "contactPhone", "email", "businessScope"));
    private final List<String> longKeys = new ArrayList<>(Arrays.asList("companyName", "engName", "creditCode", "companyType", "manageState", "establishDate", "verifyDate", "legalName", "registerCapital", "realCapital", "insureNum", "companySize", "businessScope", "registerAddress", "businessTerm", "source", "taxpayerCode", "businessRegisterCode", "organizationCode", "contactPhone", "email", "taxpayerCredential", "previousName", "province", "city", "district", "url", "industry", "firstClassify", "secondClassify", "thirdClassify", "registrar", "lon", "lat"));
    private final static Map<String, String> INDUSTRY_MAP = new HashMap<>(128);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskExecutor.submit(() -> {
            String maxId = "0";
            int size = 1000;
            while (true) {
                List<MarkerCompanyData> markList = markerCompanyDataService.limitList(maxId, size);
                markList.forEach(markData -> {
                    String fc = markData.getFirstClassify();
                    String sc = markData.getSecondClassify();
                    String tc = markData.getThirdClassify();
                    String industry = "";
                    if (StringUtils.isNotBlank(fc)) {
                        industry += fc;
                    }
                    if (StringUtils.isNotBlank(sc)) {
                        industry += sc;
                    }
                    if (StringUtils.isNotBlank(tc)) {
                        industry += tc;
                    }
                    if (INDUSTRY_MAP.get(industry) == null) {
                        INDUSTRY_MAP.put(industry, industry);
                        companyRosterIndustryService.save(new CompanyRosterIndustry(fc, sc, tc));
                    }
                });
                if (markList.size() < size) {
                    break;
                }
                maxId = markList.get(markList.size() - 1).getId();
            }

        });

        // 八方/马克数据合并到名单库
        /*taskExecutor.submit(() -> {
            int total = 0;
            int size = 1000;
//            boolean flag = true;
            String maxId = "186882584";
//            LambdaQueryWrapper<BfCompanyRoster> wrapper = Wrappers.lambdaQuery(BfCompanyRoster.class).eq(BfCompanyRoster::getState, 0);
            try {
                while (true) {
//                    Page<BfCompanyRoster> page = bfCompanyRosterService.page(new Page<>(current++, size), wrapper);
//                    taskExecutor.submit(() -> {
//                        log.info("【八方】1号子线程开始合并数据...");
//                        try {
//                            handleMergeBf(page.getRecords());
//                        } catch (Exception e) {
//                            log.error("【八方】1号子线程合并异常: ", e);
//                        }
//                    });
//                    flag = page.hasNext();
//                    if (!flag) {
//                        break;
//                    }

//                    Page<BfCompanyRoster> page1 = bfCompanyRosterService.page(new Page<>(current++, size), wrapper);
//                    taskExecutor.submit(() -> {
//                        log.info("【八方】2号子线程开始合并数据...");
//                        try {
//                            handleMergeBf(page1.getRecords());
//                        } catch (Exception e) {
//                            log.error("【八方】2号子线程合并异常: ", e);
//                        }
//                    });
//                    flag = page1.hasNext();
//                    if (!flag) {
//                        break;
//                    }
//
//                    Page<BfCompanyRoster> page2 = bfCompanyRosterService.page(new Page<>(current++, size), wrapper);
//                    taskExecutor.submit(() -> {
//                        log.info("【八方】3号子线程开始合并数据...");
//                        try {
//                            handleMergeBf(page2.getRecords());
//                        } catch (Exception e) {
//                            log.error("【八方】3号子线程合并异常: ", e);
//                        }
//                    });
//                    flag = page2.hasNext();
//                    if (!flag) {
//                        break;
//                    }
//
//                    Page<BfCompanyRoster> page3 = bfCompanyRosterService.page(new Page<>(current++, size), wrapper);
//                    taskExecutor.submit(() -> {
//                        log.info("【八方】4号子线程开始合并数据...");
//                        try {
//                            handleMergeBf(page3.getRecords());
//                        } catch (Exception e) {
//                            log.error("【八方】4号子线程合并异常: ", e);
//                        }
//                    });
//                    flag = page3.hasNext();
//                    if (!flag) {
//                        break;
//                    }

//                    log.info("【八方】主线程开始合并数据...");
//                    Page<BfCompanyRoster> page4 = bfCompanyRosterService.page(new Page<>(current++, size), wrapper);
//                    handleMergeBf(page4.getRecords());
//                    flag = page4.hasNext();


                    List<MarkerCompanyData> list = markerCompanyDataService.limitList(maxId, size);
                    int listSize = list.size();
                    total += listSize;
                    log.info("【马克】开始合并数据 {} ~ {} , maxId = {}", total - 1000, total, maxId);
                    taskExecutor.submit(() -> {
                        try {
                            handleMarkMerge(list);
                        } catch (Exception e) {
                            log.error("【马克】数据合并异常: ", e);
                        }
                    });
                    maxId = list.get(listSize - 1).getId();
                    if (listSize < 1000) {
                        break;
                    }

                    List<MarkerCompanyData> list1 = markerCompanyDataService.limitList(maxId, size);
                    listSize = list1.size();
                    total += listSize;
                    log.info("【马克】开始合并数据 {} ~ {} , maxId = {}", total - 1000, total, maxId);
                    taskExecutor.submit(() -> {
                        try {
                            handleMarkMerge(list1);
                        } catch (Exception e) {
                            log.error("【马克】数据合并异常: ", e);
                        }
                    });
                    maxId = list1.get(listSize - 1).getId();
                    if (listSize < 1000) {
                        break;
                    }

                    List<MarkerCompanyData> list2 = markerCompanyDataService.limitList(maxId, size);
                    listSize = list2.size();
                    total += listSize;
                    log.info("【马克】开始合并数据 {} ~ {} , maxId = {}", total - 1000, total, maxId);
                    taskExecutor.submit(() -> {
                        try {
                            handleMarkMerge(list2);
                        } catch (Exception e) {
                            log.error("【马克】数据合并异常: ", e);
                        }
                    });
                    maxId = list2.get(listSize - 1).getId();
                    if (listSize < 1000) {
                        break;
                    }

                    List<MarkerCompanyData> list3 = markerCompanyDataService.limitList(maxId, size);
                    listSize = list3.size();
                    total += listSize;
                    log.info("【马克】开始合并数据 {} ~ {} , maxId = {}", total - 1000, total, maxId);
                    taskExecutor.submit(() -> {
                        try {
                            handleMarkMerge(list3);
                        } catch (Exception e) {
                            log.error("【马克】数据合并异常: ", e);
                        }
                    });
                    maxId = list3.get(listSize - 1).getId();
                    if (listSize < 1000) {
                        break;
                    }

                    List<MarkerCompanyData> list4 = markerCompanyDataService.limitList(maxId, size);
                    listSize = list4.size();
                    total += listSize;
                    log.info("【马克】开始合并数据 {} ~ {} , maxId = {}", total - 1000, total, maxId);
                    try {
                        handleMarkMerge(list4);
                    } catch (Exception e) {
                        log.error("【马克】数据合并异常: ", e);
                    }
                    maxId = list4.get(listSize - 1).getId();
                    if (listSize < 1000) {
                        break;
                    }
                }
                log.info("【马克】数据合并完成...");
            } catch (Exception e) {
                log.error("【马克】异常导致循环跳出, 堆栈信息: ", e);
            }
        });*/


        // 马克数据导入
        /*taskExecutor.submit(() -> {
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
//                    try {
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
                    if (offDataItem != null && StringUtils.isNotBlank(offDataItem.getCompanyName())) {
                        offList.add(offDataItem);
                        if (offList.size() >= 300) {
                            try {
                                log.info("【注销数据】文档 {} - {} 触发批量插入数据, 结果：{}", file.getName(), rowIndex, markerCompanyOffDataService.saveBatch(offList));
                            } catch (DuplicateKeyException | DeadlockLoserDataAccessException | CannotAcquireLockException e) {
                                taskExecutor.submit(() -> handleOffDuplicateKey(new ArrayList<>(offList)));
                                log.error("【注销数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                            } catch (Exception e) {
                                if (e.toString().contains("BatchUpdateException")) {
                                    taskExecutor.submit(() -> handleOffDuplicateKey(new ArrayList<>(offList)));
                                    log.error("【注销数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                                } else {
                                    resultMsg.setCode(-1);
                                    log.error("【注销数据】处理数据异常, 堆栈: ", e);
                                }
                            }
//                                taskExecutor.submit(() -> handleOffDuplicateKey(new ArrayList<>(offList)));
                            offList.clear();
                        }
                    } else if (dataItem != null && StringUtils.isNotBlank(dataItem.getCompanyName())) {
                        list.add(dataItem);
                        if (list.size() >= 300) {
                            try {
                                log.info("【名单数据】文档 {} - {} 触发批量插入数据, 结果：{}", file.getName(), rowIndex, markerCompanyDataService.saveBatch(list));
                            } catch (DuplicateKeyException | DeadlockLoserDataAccessException | CannotAcquireLockException e) {
                                taskExecutor.submit(() -> handleDuplicateKey(new ArrayList<>(list)));
                                log.error("【名单数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                            } catch (Exception e) {
                                if (e.toString().contains("BatchUpdateException")) {
                                    taskExecutor.submit(() -> handleDuplicateKey(new ArrayList<>(list)));
                                    log.error("【名单数据】唯一键/主键冲突异常, {} ~ {} 的数据", rowIndex - 100, rowIndex);
                                } else {
                                    resultMsg.setCode(-1);
                                    log.error("【名单数据】处理数据异常, 堆栈: ", e);
                                }
                            }
//                                taskExecutor.submit(() -> handleDuplicateKey(new ArrayList<>(list)));
                            list.clear();
                        }
                    } else {
                        log.error("【名单数据】跳过文档行: {}, 数据为空", rowIndex);
                    }
//                    } catch (Exception e) {
//                        resultMsg.setCode(-1);
//                        log.error("【名单数据】处理数据异常, 堆栈: ", e);
//                    }
                });
                if (resultMsg.getCode() == 0) {
                    if (!list.isEmpty()) {
                        try {
                            log.info("【名单数据】文件 {} 最后一次批量插入数据, 结果：{}", file.getName(), markerCompanyDataService.saveBatch(list));
                        } catch (DuplicateKeyException | DeadlockLoserDataAccessException | CannotAcquireLockException e) {
                            taskExecutor.submit(() -> handleDuplicateKey(new ArrayList<>(list)));
                            log.error("【名单数据】文件 {} 最后一次批量插入, 唯一键/主键冲突异常", file.getName());
                        }
//                        log.info("【名单数据】文件 {} 最后一次插入数据......", file.getName());
//                        handleDuplicateKey(list);
                    }
                    if (!offList.isEmpty()) {
                        try {
                            log.info("【注销数据】文件 {} 最后一次批量插入注销数据, 结果：{}", file.getName(), markerCompanyOffDataService.saveBatch(offList));
                        } catch (DuplicateKeyException | DeadlockLoserDataAccessException | CannotAcquireLockException e) {
                            taskExecutor.submit(() -> handleOffDuplicateKey(new ArrayList<>(offList)));
                            log.error("【注销数据】文件 {} 最后一次批量插入, 唯一键/主键冲突异常", file.getName());
                        }
//                        log.info("【注销数据】文件 {} 最后一次插入数据......", file.getName());
//                        handleOffDuplicateKey(offList);
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
        });*/
    }

    /** 处理八方数据合并到名单库 */
    public void handleMergeBf(List<BfCompanyRoster> bfRosters) throws Exception {
//        List<CompanyRoster> rosters = new ArrayList<>();
        try {
            for (BfCompanyRoster roster : bfRosters) {
                CompanyRoster companyRoster = new CompanyRoster(roster.getCompanyName(), roster.getCreditCode(), roster.getManageState(), roster.getCompanyUrl(), roster.getContactPhone(), roster.getCompanyKeyword(), roster.getCompanyAddress(), roster.getRegisterAddress(), roster.getStaffNum(), roster.getBusinessScope());
                companyRoster.setBfProps(roster);
                try {
//                    rosters.add(companyRoster);
                    roster.setState(1);
                    companyRosterService.save(companyRoster);
                    CompanyRosterContact contact = new CompanyRosterContact(roster, companyRoster.getLegalName());
                    contact.setCompanyRosterId(companyRoster.getId());
                    try {
                        companyRosterContactService.save(contact);
                    } catch (Exception e) {
                        log.error("【八方】保存联系人数据异常, 堆栈: ", e);
                    }
                    try {
                        companyRosterSourceService.save(new CompanyRosterSource(companyRoster.getId(), CompanyRosterSourceEnum.BF));
                    } catch (Exception e) {
                        log.error("【八方】保存来源数据异常, 堆栈信息: ", e);
                    }
                } catch (DeadlockLoserDataAccessException | CannotAcquireLockException le) {
                    log.error("【八方】数据 {} 开始死锁循环插入.....", companyRoster.getName());
                    // 开启循环插入数据
                    while (true) {
                        try {
                            companyRosterService.save(companyRoster);
                        } catch (DeadlockLoserDataAccessException | CannotAcquireLockException dle) {    // 再出现死锁继续循环
                            continue;
                        } catch (DuplicateKeyException dke) {
                            handleBfDuplicate(companyRoster, roster);
                        } catch (Exception ex) {
                            log.error("【八方】死锁循环插入异常, 堆栈信息: ", ex);
                        }
                        log.error("【八方】数据 {} 结束死锁循环.....", companyRoster.getName());
                        roster.setState(1);
                        break;
                    }
                } catch (DuplicateKeyException de) {
                    handleBfDuplicate(companyRoster, roster);
                } catch (Exception ee) {
                    if (!ee.toString().contains("BadSqlGrammarException")) {
                        log.error("【八方】单条插入异常, 堆栈: ", ee);
                    }
                }
            }
//            boolean flag = companyRosterService.saveBatch(rosters);
//            if (!flag) {
//                log.error("【名单库】批量插入到名单库未知失败....");
//            }
            // 更新已添加状态
            bfCompanyRosterService.updateBatchById(bfRosters);
        } catch (Exception e) {
            log.error("组装异常: ", e);
        }
//        taskExecutor.submit(() -> {
//           List<CompanyRosterContact> contacts = new ArrayList<>();
//           List<CompanyRosterSource> sources = new ArrayList<>();
//            try {
//                rosters.forEach(roster -> {
//                    CompanyRosterContact contact = roster.getContact();
//                    contact.setCompanyRosterId(roster.getId());
//                    contacts.add(contact);
//                    sources.add(new CompanyRosterSource(roster.getId(), CompanyRosterSourceEnum.BF));
//                });
//                companyRosterContactService.saveBatch(contacts);
//                companyRosterSourceService.saveBatch(sources);
//            } catch (Exception e) {
//                log.error("【联系人&来源】批量插入联系人&来源关联数据失败, 异常堆栈: ", e);
//            }
//        });
    }

    /** 处理八方数据重复 */
    public void handleBfDuplicate(CompanyRoster companyRoster, BfCompanyRoster bfRoster) {
        CompanyRoster dbRoster = companyRosterService.getByName(companyRoster.getName());
        if (dbRoster == null) {
            log.error("【八方】主键/唯一键重复, 二次查询还是 null ？？？！！！");
        } else {
            try {
                companyRoster.setId(dbRoster.getId());
                companyRosterService.updateById(companyRoster);
                CompanyRosterContact contact = new CompanyRosterContact(bfRoster, bfRoster.getLegalName());
                contact.setCompanyRosterId(companyRoster.getId());
                try {
                    companyRosterContactService.save(contact);
                } catch (DuplicateKeyException de) {
                } catch (Exception e) {
                    log.error("【八方】保存联系人数据异常, 堆栈: ", e);
                }
            } catch (Exception e) {
                log.error("【八方】更新数据出现异常, 堆栈信息: ", e);
            }
        }
    }

    /** 处理马克数据合并到名单库 */
    public void handleMarkMerge(List<MarkerCompanyData> markRosters) {
        List<CompanyRoster> rosters = new ArrayList<>();
        for (MarkerCompanyData markRoster : markRosters) {
            CompanyRoster companyRoster = new CompanyRoster(markRoster.getCompanyName(), markRoster.getCreditCode(), markRoster.getManageState(), markRoster.getUrl(), markRoster.getContactPhone(), "", "", markRoster.getRegisterAddress(), markRoster.getCompanySize(), markRoster.getBusinessScope());
            companyRoster.setMarkProps(markRoster);
            rosters.add(companyRoster);
        }
        try {
            while (true) {
                try {
                    boolean flag = companyRosterService.saveBatch(rosters);
                    if (flag) {
                        List<CompanyRosterSource> list = new ArrayList<>(rosters.size());
                        rosters.forEach(item -> list.add(new CompanyRosterSource(item.getId(), CompanyRosterSourceEnum.MARK)));
                        companyRosterSourceService.saveBatch(list);
                    } else {
                        log.error("【马克数据】批量合并失败, 数据库添加结果为 false, 未出现异常");
                    }
                    break;
                } catch (CannotAcquireLockException | DeadlockLoserDataAccessException e) {
                }
            }
        } catch (DuplicateKeyException de) {
            for (CompanyRoster roster : rosters) {
                try {
                    while (true) {
                        try {
                            companyRosterService.save(roster);
                            companyRosterSourceService.save(new CompanyRosterSource(roster.getId(), CompanyRosterSourceEnum.MARK));
                            break;
                        } catch (CannotAcquireLockException e) {
                        }
                    }
                } catch (DuplicateKeyException dke) {
                    try {
                        CompanyRoster dbRoster = companyRosterService.getByName(roster.getName());
                        if (dbRoster != null && StringUtils.isNotBlank(dbRoster.getId())) {
                            roster.setId(dbRoster.getId());
                            companyRosterService.updateById(roster);
                        } else {
                            while (true) {
                                try {
                                    companyRosterService.save(roster);
                                    companyRosterSourceService.save(new CompanyRosterSource(roster.getId(), CompanyRosterSourceEnum.MARK));
                                    break;
                                } catch (CannotAcquireLockException e) {
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("【马克数据】根据企业名称 {} 更新数据失败, 异常堆栈: ", roster.getName(), e);
                    }
                } catch (Exception e) {
                    log.error("【马克数据】企业 {} 插入失败, 异常堆栈: ", roster.getName(), e);
                }
            }
        } catch (Exception e) {
            log.error("【马克数据】批量插入数据失败, 异常堆栈: ", e);
        }
    }

    /**
     拆分100条/次
     */
    public void handleDuplicateKey(List<MarkerCompanyData> dataList) {
//        taskExecutor.submit(() -> handleSubDuplicateKey(dataList));
        handleSubDuplicateKey(dataList);

//        List<MarkerCompanyData> subList = new ArrayList<>();
//        dataList.forEach(data -> {
//            subList.add(data);
//            if (subList.size() >= 100) {
////                taskExecutor.submit(() -> handleSubDuplicateKey(new ArrayList<>(subList)));
//                handleSubDuplicateKey(subList);
//                subList.clear();
//            }
//        });
//        if (!subList.isEmpty()) {
////            taskExecutor.submit(() -> handleSubDuplicateKey(subList));
//            handleSubDuplicateKey(subList);
//        }
    }

    public void handleSubDuplicateKey(List<MarkerCompanyData> subList) {
        try {
            log.info("【数据冲突】文档数据冲突, 小批量插入数据, 结果：{}, 首条数据名称: {}", markerCompanyDataService.saveBatch(subList), subList.get(0).getCompanyName());
        } catch (Exception e) {
            List<MarkerCompanyData> updateList = new ArrayList<>();
            Map<String, String> map = new HashMap<>(100);
            subList.forEach(sub -> {
                // 小批量数据里面可能有相同企业
                String existId = map.get(sub.getCompanyName());
                if (StringUtils.isNotBlank(existId)) {
                    sub.setId(existId);
                    updateList.add(sub);
                    return;
                }
                Wrapper<MarkerCompanyData> wrapper = Wrappers.lambdaQuery(MarkerCompanyData.class).eq(MarkerCompanyData::getCompanyName, sub.getCompanyName());
                MarkerCompanyData dbData = markerCompanyDataService.getOne(wrapper);
                try {
                    if (dbData != null) {
                        sub.setId(dbData.getId());
                        updateList.add(sub);
                    } else {
                        sub.setId(null);
//                        markerCompanyDataService.getBaseMapper().insertData(sub.getCompanyName(), sub.getEngName(), sub.getManageState(), sub.getLegalName(), sub.getRegisterCapital(), sub.getRealCapital(), sub.getEstablishDate(), sub.getVerifyDate(), sub.getBusinessTerm(), sub.getProvince(), sub.getCity(), sub.getDistrict(), sub.getRegistrar(), sub.getCreditCode(), sub.getTaxpayerCode(), sub.getBusinessRegisterCode(), sub.getOrganizationCode(), sub.getTaxpayerCredential(), sub.getInsureNum(), sub.getCompanyType(), sub.getPreviousName(), sub.getRegisterAddress(), sub.getUrl(), sub.getContactPhone(), sub.getEmail(), sub.getBusinessScope(), sub.getCompanySize(), sub.getSource(), sub.getIndustry(), sub.getFirstClassify(), sub.getSecondClassify(), sub.getThirdClassify(), sub.getLon(), sub.getLat());
                        map.put(sub.getCompanyName(), sub.getId());
                    }
                } catch (DuplicateKeyException de) {
                    dbData = markerCompanyDataService.getOne(wrapper);
                    if (dbData == null) {
                        log.error("【名单数据】主键/唯一键重复, 二次查询还是 null ？？？！！！");
                    } else {
                        sub.setId(dbData.getId());
                        updateList.add(sub);
                    }
                } catch (DeadlockLoserDataAccessException | CannotAcquireLockException le) {
                    log.error("【名单数据】数据 {} 开始死锁循环插入.....", sub.getCompanyName());
                    // 开启循环插入数据
                    while (true) {
                        try {
                            markerCompanyDataService.save(sub);
                        } catch (DeadlockLoserDataAccessException | CannotAcquireLockException dle) {    // 再出现死锁继续循环
                            continue;
                        } catch (DuplicateKeyException dke) {
                            dbData = markerCompanyDataService.getOne(wrapper);
                            if (dbData == null) {
                                log.error("【注销数据】主键/唯一键重复, 二次查询还是 null ？？？！！！");
                            } else {
                                sub.setId(dbData.getId());
                                updateList.add(sub);
                            }
                        } catch (Exception ex) {
                            log.error("【注销数据】死锁循环插入异常, 堆栈信息: ", ex);
                        }
                        log.error("【名单数据】数据 {} 结束死锁循环.....", sub.getCompanyName());
                        break;
                    }
                } catch (Exception ee) {
                    if (!e.toString().contains("BadSqlGrammarException")) {
                        log.error("【名单数据】单条插入异常, 堆栈: ", ee);
                    }
                }
            });
            if (!updateList.isEmpty()) {
                try {
                    markerCompanyDataService.updateBatchById(updateList);
                } catch (DeadlockLoserDataAccessException | CannotAcquireLockException le) {
                    log.error("【名单数据】批量 updateBatchById 数据开始死锁循环.....");
                    // 循环 update
                    while (true) {
                        try {
                            markerCompanyDataService.updateBatchById(updateList);
                        } catch (DeadlockLoserDataAccessException | CannotAcquireLockException dle) {
                            continue;
                        } catch (Exception ex) {
                            log.error("【名单数据】死锁循环插入异常, 堆栈信息: ", ex);
                        }
                        break;
                    }
                    log.error("【名单数据】批量 updateBatchById 数据结束死锁循环.....");
                } catch (Exception ex) {
                    log.error("【名单数据】根据id批量更新失败, 异常堆栈:　", ex);
                }
            }
        }
    }

    /**
     拆分100条/次
     */
    public void handleOffDuplicateKey(List<MarkerCompanyOffData> dataList) {
//        taskExecutor.submit(() -> handleOffSubDuplicateKey(dataList));
        handleOffSubDuplicateKey(dataList);

//        List<MarkerCompanyOffData> subList = new ArrayList<>();
//        dataList.forEach(data -> {
//            subList.add(data);
//            if (subList.size() >= 100) {
////                taskExecutor.submit(() -> handleOffSubDuplicateKey(new ArrayList<>(subList)));
//                handleOffSubDuplicateKey(subList);
//                subList.clear();
//            }
//        });
//        if (!subList.isEmpty()) {
////            taskExecutor.submit(() -> handleOffSubDuplicateKey(subList));
//            handleOffSubDuplicateKey(subList);
//        }
    }

    public void handleOffSubDuplicateKey(List<MarkerCompanyOffData> subList) {
        try {
            log.info("【注销数据冲突】文档数据冲突, 小批量插入数据, 结果：{}, 首条数据名称: {}", markerCompanyOffDataService.saveBatch(subList), subList.get(0).getCompanyName());
        } catch (Exception e) {
            List<MarkerCompanyOffData> updateList = new ArrayList<>();
            Map<String, String> map = new HashMap<>(100);
            subList.forEach(sub -> {
                // 小批量数据里面可能有相同企业
                String existId = map.get(sub.getCompanyName());
                if (StringUtils.isNotBlank(existId)) {
                    sub.setId(existId);
                    updateList.add(sub);
                    return;
                }
                Wrapper<MarkerCompanyOffData> wrapper = Wrappers.lambdaQuery(MarkerCompanyOffData.class).eq(MarkerCompanyOffData::getCompanyName, sub.getCompanyName());
                MarkerCompanyOffData dbOffData = markerCompanyOffDataService.getOne(wrapper);
                try {
                    if (dbOffData != null) {
                        sub.setId(dbOffData.getId());
                        updateList.add(sub);
                    } else {
                        sub.setId(null);
                        markerCompanyOffDataService.save(sub);
//                        markerCompanyOffDataService.getBaseMapper().insertData(sub.getCompanyName(), sub.getEngName(), sub.getManageState(), sub.getLegalName(), sub.getRegisterCapital(), sub.getRealCapital(), sub.getEstablishDate(), sub.getVerifyDate(), sub.getBusinessTerm(), sub.getProvince(), sub.getCity(), sub.getDistrict(), sub.getRegistrar(), sub.getCreditCode(), sub.getTaxpayerCode(), sub.getBusinessRegisterCode(), sub.getOrganizationCode(), sub.getTaxpayerCredential(), sub.getInsureNum(), sub.getCompanyType(), sub.getPreviousName(), sub.getRegisterAddress(), sub.getUrl(), sub.getContactPhone(), sub.getEmail(), sub.getBusinessScope(), sub.getCompanySize(), sub.getSource(), sub.getIndustry(), sub.getFirstClassify(), sub.getSecondClassify(), sub.getThirdClassify(), sub.getLon(), sub.getLat());
                        map.put(sub.getCompanyName(), sub.getId());
                    }
                } catch (DuplicateKeyException de) {
                    dbOffData = markerCompanyOffDataService.getOne(wrapper);
                    if (dbOffData == null) {
                        log.error("【注销数据】主键/唯一键重复, 二次查询还是 null ？？？！！！");
                    } else {
                        sub.setId(dbOffData.getId());
                        updateList.add(sub);
                    }
                } catch (DeadlockLoserDataAccessException | CannotAcquireLockException le) {
                    log.error("【注销数据】数据 {} 开始死锁循环插入.....", sub.getCompanyName());
                    // 开启循环插入数据
                    while (true) {
                        try {
                            markerCompanyOffDataService.save(sub);
                        } catch (DeadlockLoserDataAccessException | CannotAcquireLockException dle) {    // 再出现死锁继续循环
                            continue;
                        } catch (DuplicateKeyException dke) {
                            dbOffData = markerCompanyOffDataService.getOne(wrapper);
                            if (dbOffData == null) {
                                log.error("【注销数据】主键/唯一键重复, 二次查询还是 null ？？？！！！");
                            } else {
                                sub.setId(dbOffData.getId());
                                updateList.add(sub);
                            }
                        } catch (Exception ex) {
                            log.error("【注销数据】死锁循环插入异常, 堆栈信息: ", ex);
                        }
                        log.error("【注销数据】数据 {} 结束死锁循环.....", sub.getCompanyName());
                        break;
                    }
                } catch (Exception ee) {
                    if (!ee.toString().contains("BadSqlGrammarException")) {
                        log.error("【注销数据】单条插入异常, 堆栈: ", ee);
                    }
                }
            });
            try {
                markerCompanyOffDataService.updateBatchById(updateList);
            } catch (DeadlockLoserDataAccessException | CannotAcquireLockException le) {
                log.error("【注销数据】批量 updateBatchById 数据开始死锁循环.....");
                // 循环 update
                while (true) {
                    try {
                        markerCompanyOffDataService.updateBatchById(updateList);
                    } catch (DeadlockLoserDataAccessException | CannotAcquireLockException dle) {
                        continue;
                    } catch (Exception ex) {
                        log.error("【注销数据】死锁循环插入异常, 堆栈信息: ", ex);
                    }
                    break;
                }
                log.error("【注销数据】批量 updateBatchById 数据结束死锁循环.....");
            } catch (Exception ex) {
                log.error("【注销数据】根据id批量更新失败, 异常堆栈:　", ex);
            }
        }
    }
}
