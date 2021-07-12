 {
     "type": "index",
     "spec": {
         "dataSchema": {
             "dataSource": "${datasource}",
             "parser": {
                 "type": "string",
                 "parseSpec": {
                     "format": "tsv",
                     "timestampSpec": {
                         "column": "${timestampSpec.column}",
                         "format": "${timestampSpec.format}"
                     },
                     "columns": [
                        <#list columns as column>
                        "${column.name}",
                        </#list>
                        "${timestampSpec.column}"
                     ],
                     "dimensionsSpec": {
                         "dimensions": [
                            <#list columns as column>
                            {
                                "name": "${column.name}",
                                "type": "${column.type}"
                            }
                            </#list>
                         ]
                     }
                 }
             },
             "granularitySpec": {
                 "type": "uniform",
                 "intervals": [
                     "1992-01-02/1998-12-01"
                 ],
                 "segmentGranularity": "year",
                 "queryGranularity": "day"
             }
         },
         "ioConfig": {
             "type": "index",
             "firehose": {
                 "type": "local",
                 "baseDir": "/opt/druid/var/",
                 "filter": "${datasource}.tsv"
             },
             "appendToExisting": false
         },
         "tuningConfig": {
             "type": "index",
             "maxRowsPerSegment": 5000000,
             "maxRowsInMemory": 250000,
             "segmentWriteOutMediumFactory": {
                 "type": "offHeapMemory"
             }
         }
     }
 }
