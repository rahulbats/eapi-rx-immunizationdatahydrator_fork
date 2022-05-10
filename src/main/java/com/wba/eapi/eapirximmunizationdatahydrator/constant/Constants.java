package com.wba.eapi.eapirximmunizationdatahydrator.constant;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * The type Constants.
 */
@Component
public class Constants {

  public static final String RX_NBR = "RX_NBR";
  public static final String STORE_NBR = "STORE_NBR";
  public static final String COMPARISON_DATE_FIELD_PRIMARY = "FILL_UPDATE_DTTM";
  public static final String COMPARISON_DATE_FIELD_SECONDARY = "RX_UPDATE_DTTM";
  public static final String COMPARISON_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
  public static final String FILL_SOLD_DTTM="FILL_SOLD_DTTM";
  public static final String RX = "RX";
  public static final String FILL = "FILL";
  public static final String DRUG = "DRUG";
  public static final String DATA = "data";
  public static final String PAT_ID = "PAT_ID";
  public static final String RX_RAW_TABLE = "RxRawTable";
  public static final String FILL_RAW_TABLE = "FillRawTable";
  public static final String RX_FILL_JOIN = "RxFillJoin";
  public static final String PAT_ID_AGGREGATOR = "PatIdAggregator";

  //Common fields in Rx and Fill
  public static final String CREATE_USER_ID = "CREATE_USER_ID";
  public static final String CREATE_DTTM = "CREATE_DTTM";
  public static final String UPDATE_USER_ID = "UPDATE_USER_ID";
  public static final String UPDATE_DTTM = "UPDATE_DTTM";

  //Rx fields
  public static final String RX_CREATE_USER_ID = "RX_CREATE_USER_ID";
  public static final String RX_CREATE_DTTM = "RX_CREATE_DTTM";
  public static final String RX_UPDATE_USER_ID = "RX_UPDATE_USER_ID";
  public static final String RX_UPDATE_DTTM = "RX_UPDATE_DTTM";
  public static final String RX_IMMU_IND = "RX_IMMU_IND";


  //Fill fields
  public static final String FILL_UPDATE_USER_ID = "FILL_UPDATE_USER_ID";
  public static final String FILL_UPDATE_DTTM = "FILL_UPDATE_DTTM";


  //Info
  public static final String DATA_VALIDATION_SUCCESS = "SVC-LOG-001";
  public static final String TOPIC_JOIN_SUCCESS = "SVC-LOG-002";
  public static final String KFK_PUBLISH_SUCCESS = "KFK-LOG-001";
  public static final String INPUT_EVENT_VALIDATION_FAILED = "INPUT EVENT VALIDATION FAILED";
  public static final String INPUT_VALIDATION_FAILED = "INPUT VALIDATION FAILED";
  public static final String EVENT_VALIDATION_SUCCESS = "EVENT VALIDATION SUCCESS";
  public static final String RX_FILL_JOIN_FAILED = "RX-FILL JOIN FAILED";
  public static final String GROUP_BY_PAT_ID_FAILED = "GROUP BY PAT_ID FAILED";
  public static final String SUCCESSFULLY_HYDRATED_RECORD_FOR_PAT_ID = "SUCCESSFULLY HYDRATED RECORD FOR PAT_ID";
  public static final String EVENT_TIME = "event_time";
  public static final String EVENT_COUNT = "event_count";
  public static final String YES = "yes";
  public static final String INFO = "INFO";
  public static final String INPUT = "INPUT";
  public static final String EXCEPTION = "Exception";
  public static final String OUTPUT_TOPIC_PUBLISH_FAILED = "OUTPUT TOPIC PUBLISH FAILED";

  //Error
  public static final String ERROR = "ERROR";
  public static final String DATA_VALIDATION_ERROR = "SVC-ERR-001";
  public static final String SVC_ERROR = "SVC-ERR-002";
  public static final String TOPIC_JOIN_FAILED = "SVC-ERR-002";
  public static final String KFK_PUBLISH_FAILURE = "KFK-ERR-001";
  public static final String FAILURE = "Error Occurred in Stream Operations";

  //Loggers
  public static final String SERVICE_NAME = "eapirximmunizationdatahydrator";
  public static final String SERVICE_VERSION = "v1_0_0";
  public static final String TX_ID = "1.0-eapi-rx-immunization-data-hydrator";
  public static final String CORRELATION_ID = UUID.randomUUID().toString();

  public static final String ORGANIZATION = "wba";
  public static final String REVISION = "v1_0_0";
  public static final String ENV = "Harmony";
  public static final String MICROSERVICE = "eapirximmunizationdatahydrator";
  public static final String APP_V = "Harmony";

}
