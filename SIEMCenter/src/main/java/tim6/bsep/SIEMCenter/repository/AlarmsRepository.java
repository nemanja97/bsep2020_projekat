package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.web.v1.dto.ReportData;

import java.util.List;

@Repository
public interface AlarmsRepository extends MongoRepository<Alarm, Long>, QuerydslPredicateExecutor<Alarm> {

    Alarm findFirstByOrderByTimestampDesc();

    @Aggregation(pipeline={"{$project: {\n" +
            "  startDate: { $dateFromString: {\n" +
            "     dateString: ?0,\n" +
            "}},\n" +
            "  endDate: { $dateFromString: {\n" +
            "     dateString: ?1,\n" +
            "}},\n" +
            "  timestamp: '$timestamp',\n" +
            "  type: '$type'\n" +
            "}}", "{$project: {\n" +
            "  cond:{$cond: {\n" +
            "    if: {\n" +
            "      $and:[\n" +
            "      {$gt:['$timestamp', '$startDate']},\n" +
            "      {$lt:['$timestamp', '$endDate']}\n" +
            "      ]\n" +
            "    },\n" +
            "    then: 1,\n" +
            "    else: 2\n" +
            "  }\n" +
            "  },\n" +
            "  timestamp: \"$timestamp\",\n" +
            "  type: \"$type\"\n" +
            "}}", "{$match: {\n" +
            "  cond:{\n" +
            "    $eq: 1\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  year: {'$year': '$timestamp'},\n" +
            "  month: {'$month': '$timestamp'},\n" +
            "  type: '$type'\n" +
            "}}", "{$group: {\n" +
            "  _id: {year: '$year', month:'$month', type: '$type'},\n" +
            "  count: { $sum: 1}\n" +
            "}}", "{$addFields: {\n" +
            "  month: {\n" +
            "    $let: {\n" +
            "      vars: {\n" +
            "        monthsInString: ['.', 'Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'Jun.',\n" +
            "        'Jul.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.']\n" +
            "      },\n" +
            "      in: {\n" +
            "        $arrayElemAt: ['$$monthsInString', '$_id.month']\n" +
            "        }\n" +
            "      }\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  _id: 1,\n" +
            "  date: { $concat: ['$month', ' ', {$toString: '$_id.year'}]},\n" +
            "  type: '$_id.type',\n" +
            "  simCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SIMULATED']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SYSTEM']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  }\n" +
            "}}", "{$group: {\n" +
            "  _id: '$date',\n" +
            "  simCount: {\n" +
            "    '$sum': '$simCount'\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    '$sum' : '$sysCount'\n" +
            "  },\n" +
            "  year : {'$first': '$_id.year'},\n" +
            "  month : {'$first': '$_id.month'}\n" +
            "}}", "{$sort: {\n" +
            "  'year': 1,\n" +
            "  'month': 1,\n" +
            "}}", "{$project: {\n" +
            "  _id: 0,\n" +
            "  date: '$_id',\n" +
            "  simCount: '$simCount',\n" +
            "  sysCount: '$sysCount',\n" +
            "}}"})
    List<ReportData> findAlarmsMonthlyCount(String startDate, String endDate);

    @Aggregation(pipeline={"{$project: {\n" +
            "  startDate: { $dateFromString: {\n" +
            "     dateString: '?0',\n" +
            "}},\n" +
            "  endDate: { $dateFromString: {\n" +
            "     dateString: '?1',\n" +
            "}},\n" +
            "  timestamp: '$timestamp',\n" +
            "  type: '$type'\n" +
            "}}", "{$project: {\n" +
            "  cond:{$cond: {\n" +
            "    if: {\n" +
            "      $and:[\n" +
            "      {$gt:['$timestamp', '$startDate']},\n" +
            "      {$lt:['$timestamp', '$endDate']}\n" +
            "      ]\n" +
            "    },\n" +
            "    then: 1,\n" +
            "    else: 2\n" +
            "  }\n" +
            "  },\n" +
            "  timestamp: \"$timestamp\",\n" +
            "  type: \"$type\"\n" +
            "}}", "{$match: {\n" +
            "  cond:{\n" +
            "    $eq: 1\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  year: {'$year': '$timestamp'},\n" +
            "  month: {'$month': '$timestamp'},\n" +
            "  weekOfMonth: {$floor: {$divide: [{$dayOfMonth: '$timestamp'}, 7]}},\n" +
            "  type: '$type'\n" +
            "}}", "{$group: {\n" +
            "  _id: {year: '$year', month:'$month', week:'$weekOfMonth', type: '$type'},\n" +
            "  count: { $sum: 1}\n" +
            "}}", "{$addFields: {\n" +
            "  month: {\n" +
            "    $let: {\n" +
            "      vars: {\n" +
            "        monthsInString: ['.', 'Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'Jun.',\n" +
            "        'Jul.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.']\n" +
            "      },\n" +
            "      in: {\n" +
            "        $arrayElemAt: ['$$monthsInString', '$_id.month']\n" +
            "        }\n" +
            "      }\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  _id: 1,\n" +
            "  date: { $concat: [{$toString: '$_id.week'}, '. week ', '$month', ' ', {$toString: '$_id.year'}]},\n" +
            "  type: '$_id.type',\n" +
            "  simCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SIMULATED']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SYSTEM']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  }\n" +
            "}}", "{$group: {\n" +
            "  _id: '$date',\n" +
            "  simCount: {\n" +
            "    '$sum': '$simCount'\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    '$sum' : '$sysCount'\n" +
            "  },\n" +
            "  year : {'$first': '$_id.year'},\n" +
            "  month : {'$first': '$_id.month'},\n" +
            "  week: {'$first': '$_id.week'}\n" +
            "}}", "{$sort: {\n" +
            "  'year': 1,\n" +
            "  'month': 1,\n" +
            "  'week': 1\n" +
            "}}", "{$project: {\n" +
            "  _id: 0,\n" +
            "  date: '$_id',\n" +
            "  simCount: '$simCount',\n" +
            "  sysCount: '$sysCount',\n" +
            "}}"})
    List<ReportData> findAlarmsWeeklyCount(String startDate, String endDate);


    @Aggregation(pipeline={"" +
            "{$project: {\n" +
            "  startDate: { $dateFromString: {\n" +
            "     dateString: ?0,\n" +
            "}},\n" +
            "  endDate: { $dateFromString: {\n" +
            "     dateString: ?1,\n" +
            "}},\n" +
            "  timestamp: '$timestamp',\n" +
            "  type: '$type'\n" +
            "}}", "{$project: {\n" +
            "  cond:{$cond: {\n" +
            "    if: {\n" +
            "      $and:[\n" +
            "      {$gt:['$timestamp', '$startDate']},\n" +
            "      {$lt:['$timestamp', '$endDate']}\n" +
            "      ]\n" +
            "    },\n" +
            "    then: 1,\n" +
            "    else: 2\n" +
            "  }\n" +
            "  },\n" +
            "  timestamp: \"$timestamp\",\n" +
            "  type: \"$type\"\n" +
            "}}", "{$match: {\n" +
            "  cond:{\n" +
            "    $eq: 1\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  year: {'$year': '$timestamp'},\n" +
            "  month: {'$month': '$timestamp'},\n" +
            "  dayOfMonth: {'$dayOfMonth': '$timestamp'},\n" +
            "  type: '$type'\n" +
            "}}", "{$group: {\n" +
            "  _id: {year: '$year', month:'$month', day:'$dayOfMonth', type: '$type'},\n" +
            "  count: { $sum: 1}\n" +
            "}}", "{$addFields: {\n" +
            "  month: {\n" +
            "    $let: {\n" +
            "      vars: {\n" +
            "        monthsInString: ['.', 'Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'Jun.',\n" +
            "        'Jul.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.']\n" +
            "      },\n" +
            "      in: {\n" +
            "        $arrayElemAt: ['$$monthsInString', '$_id.month']\n" +
            "        }\n" +
            "      }\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  _id: 1,\n" +
            "  date: { $concat: [{$toString: '$_id.day'}, '. ', '$month', ' ', {$toString: '$_id.year'}]},\n" +
            "  type: '$_id.type',\n" +
            "  simCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SIMULATED']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    $cond: {\n" +
            "      if : {\n" +
            "        $eq:['$_id.type', 'SYSTEM']\n" +
            "      },\n" +
            "      then: '$count',\n" +
            "      else: 0\n" +
            "    }\n" +
            "  }\n" +
            "}}", "{$group: {\n" +
            "  _id: '$date',\n" +
            "  simCount: {\n" +
            "    '$sum': '$simCount'\n" +
            "  },\n" +
            "  sysCount: {\n" +
            "    '$sum' : '$sysCount'\n" +
            "  },\n" +
            "  year : {'$first': '$_id.year'},\n" +
            "  month : {'$first': '$_id.month'},\n" +
            "  day: {'$first': '$_id.day'}\n" +
            "}}", "{$sort: {\n" +
            "  'year': 1,\n" +
            "  'month': 1,\n" +
            "  'day': 1\n" +
            "}}", "{$project: {\n" +
            "  _id: 0,\n" +
            "  date: '$_id',\n" +
            "  simCount: '$simCount',\n" +
            "  sysCount: '$sysCount',\n" +
            "}}"})
    List<ReportData> findAlarmsDailyCount(String startDate, String endDate);

    @Aggregation(pipeline={"" +
            "{$project: {\n" +
            "  startDate: { $dateFromString: {\n" +
            "    dateString: ?0\n" +
            "  }},\n" +
            "  endDate: { $dateFromString: {\n" +
            "    dateString: ?1\n" +
            "  }},\n" +
            "  timestamp: '$timestamp',\n" +
            "  type: '$type'\n" +
            "}}", "{$project: {\n" +
            "  cond:{$cond: {\n" +
            "    if: {\n" +
            "         $and:[\n" +
            "            {$gt:['$timestamp', '$startDate']},\n" +
            "            {$lt:['$timestamp', '$endDate']}\n" +
            "            ]},\n" +
            "              then: 1,\n" +
            "              else: 2\n" +
            "        }\n" +
            "      },\n" +
            "  type: '$type'\n" +
            "}}", "{$match: {\n" +
            "  cond:{\n" +
            "    $eq: 1\n" +
            "  }\n" +
            "}}", "{$group: {\n" +
            "  _id: '$type',\n" +
            "  count: {\n" +
            "    '$sum': 1\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  _id: 0,\n" +
            "  type: '$_id',\n" +
            "  count: '$count'\n" +
            "\n" +
            "  }}"})
    List<ReportData> findAlarmsTotalCountByType(String startDate, String endDate);


    @Aggregation(pipeline={"{$project: {\n" +
            "  startDate: { $dateFromString: {\n" +
            "    dateString: ?0\n" +
            "  }},\n" +
            "  endDate: { $dateFromString: {\n" +
            "    dateString: ?1\n" +
            "  }},\n" +
            "  timestamp: '$timestamp',\n" +
            "  hostnames: '$hostnames'\n" +
            "}}", "{$unwind: {\n" +
            "  path: \"$hostnames\",\n" +
            "}}", "{$project: {\n" +
            "  cond:{$cond: {\n" +
            "    if: {\n" +
            "         $and:[\n" +
            "            {$gt:['$timestamp', '$startDate']},\n" +
            "            {$lt:['$timestamp', '$endDate']}\n" +
            "            ]},\n" +
            "              then: 1,\n" +
            "              else: 2\n" +
            "        }\n" +
            "      },\n" +
            "  hostnames: '$hostnames'\n" +
            "}}", "{$match: {\n" +
            "  cond:{\n" +
            "    $eq: 1\n" +
            "  }\n" +
            "}}", "{$group: {\n" +
            "  _id: '$hostnames',\n" +
            "  count: {\n" +
            "    '$sum': 1\n" +
            "  }\n" +
            "}}", "{$project: {\n" +
            "  _id: 0,\n" +
            "  hostname: '$_id',\n" +
            "  count: '$count'\n" +
            "\n" +
            "  }}"})
    List<ReportData> findAlarmsTotalCountByHostname(String startDate, String endDate);

}
