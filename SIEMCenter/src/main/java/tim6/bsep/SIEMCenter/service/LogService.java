package tim6.bsep.SIEMCenter.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.web.v1.dto.ReportData;

import java.util.List;

public interface LogService {

    Page<Log> findPredicate(Predicate predicate, Pageable pageable);

    Log findById(Long id);

    void save(Log log);

    List<ReportData> getLogsMonthlyCount(String startDate, String endDate);

    List<ReportData> getLogsWeeklyCount(String startDate, String endDate);

    List<ReportData> getLogsDailyCount(String startDate, String endDate);

    List<ReportData> getLogsTotalCountByType(String startDate, String endDate);

    List<ReportData> getLogsTotalCountByHostname(String startDate, String endDate);
}
