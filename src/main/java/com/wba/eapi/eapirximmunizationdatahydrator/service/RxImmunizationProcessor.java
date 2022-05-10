package com.wba.eapi.eapirximmunizationdatahydrator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wba.eapi.eapirximmunizationdatahydrator.common.LoggingUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants;
import com.wba.eapi.eapirximmunizationdatahydrator.exception.InvalidDateException;
import com.wba.eapi.eapirximmunizationdatahydrator.model.MergedRecord;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Rx Immunization Hydrator Processor.
 */
@RefreshScope
@Component
public class RxImmunizationProcessor {

    @Autowired
    public LoggingUtils loggingUtil;
    @Autowired
    public ObjectMapper mapper;

    long startTime = 0;

    /**
     * validateMsg - Validates the input event.
     *
     * @param v       - Data from Stream
     * @param msgType - Message Type
     * @return data - Validated data in String format
     */
    public boolean validateMsg(GenericRecord v, String msgType) {
        startTime = System.nanoTime();
        try {
            GenericRecord data = (GenericRecord) v.get(Constants.DATA);
            String transId = null;
            // check fill_sold_date field for null
            if (data == null || data.toString().equals("")) {
                loggingUtil.debug(msgType + Constants.INPUT_EVENT_VALIDATION_FAILED + " : Input data object is null", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                return false;
            }
            if(msgType.equals(Constants.RX)) {
                if (data.get(Constants.STORE_NBR) != null && data.get(Constants.RX_NBR) != null && data.get(Constants.RX_IMMU_IND).toString().equals("Y")  && data.get(Constants.FILL_SOLD_DTTM)!=null)
                    return true;
            } else if (data.get(Constants.STORE_NBR) != null && data.get(Constants.RX_NBR) != null)
                return true;
            else
                return false;

            loggingUtil.debug(msgType + Constants.DATA_VALIDATION_SUCCESS + " : Successfully validated input event : " + transId,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        } catch (Exception e) {
            loggingUtil.error(Constants.INPUT_VALIDATION_FAILED + Constants.DATA_VALIDATION_ERROR + e,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * mergeRxFill - Merges RX and FILL Datas that have  similar keys into one generic record.
     *
     * @param rx   - RX Stream Data
     * @param fill - Fill Stream Data
     * @return genericRecord - Merged Generic Record.
     */
    public String mergeRxFill(String rx, String fill) {
        Schema schema = ReflectData.get().getSchema(MergedRecord.class);
        GenericRecord genericRecord = new GenericData.Record(schema);
        startTime = System.nanoTime();
        try {
            JSONObject rxObj = new JSONObject(rx);
            loggingUtil.debug("Merging Data for : " + rxObj.get(Constants.STORE_NBR).toString() + rxObj.get(Constants.RX_NBR).toString(),
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            rxObj.keySet().forEach(k -> {
                if (k.equals(Constants.CREATE_USER_ID))
                    genericRecord.put(Constants.RX_CREATE_USER_ID, rxObj.get(k));
                else if (k.equals(Constants.CREATE_DTTM))
                    genericRecord.put(Constants.RX_CREATE_DTTM, rxObj.get(k));
                else if (k.equals(Constants.UPDATE_USER_ID))
                    genericRecord.put(Constants.RX_UPDATE_USER_ID, rxObj.get(k));
                else if (k.equals(Constants.UPDATE_DTTM))
                    genericRecord.put(Constants.RX_UPDATE_DTTM, rxObj.get(k));
                else {
                    if (genericRecord.getSchema().getField(k) != null) {
                        genericRecord.put(k, rxObj.get(k));
                        loggingUtil.debug("Merged RX Data for key : " + k, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                    }
                }
            });

            JSONObject fillObj = new JSONObject(fill);
            fillObj.keySet().forEach(k -> {
                if (k.equals(Constants.UPDATE_USER_ID))
                    genericRecord.put(Constants.FILL_UPDATE_USER_ID, fillObj.get(k));
                else if (k.equals(Constants.UPDATE_DTTM))
                    genericRecord.put(Constants.FILL_UPDATE_DTTM, fillObj.get(k));
                else {
                    if (genericRecord.getSchema().getField(k) != null) {
                        genericRecord.put(k, fillObj.get(k));
                        loggingUtil.debug("Merged Fill Data for key : " + k, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                    }
                }
            });
            loggingUtil.debug("Merged Data for : " + rxObj.get(Constants.STORE_NBR).toString() + rxObj.get(Constants.RX_NBR).toString(),
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);

        } catch (Exception e) {
            loggingUtil.error(Constants.RX_FILL_JOIN_FAILED + Constants.TOPIC_JOIN_FAILED + e,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        }
        return genericRecord.toString();

    }

    /**
     * filterPatId - groups data by PatId
     *
     * @param k - Key
     * @param v - Value
     * @return keyValue - Map of PatId as key and data as its value
     */
    public KeyValue<String, String> filterPatId(String k, String v) {
        KeyValue<String, String> keyValue = null;
        try {
            JSONObject jo = new JSONObject(v);
            String patId = (String) jo.get(Constants.PAT_ID);
            loggingUtil.debug("Group By PatId - " + patId, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            keyValue = KeyValue.pair(patId, v);
        } catch (Exception e) {
            loggingUtil.error(Constants.GROUP_BY_PAT_ID_FAILED + Constants.SVC_ERROR + e,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        }
        return keyValue;
    }

    /**
     * @param aggValue - current agg value from reduce function
     * @param newValue - current new value from reduce function
     * @return - aggValue concatenated with new value - deduplicated
     * @throws ParseException       - throws when unable to parse
     * @throws InvalidDateException - throws when invalid date string
     */
    public String concatenateWithDedup(String aggValue, String newValue) throws ParseException, InvalidDateException {
        loggingUtil.debug("concatenateWithDedup :: Starting dedup concatination with newValue", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        JSONArray aggValueJsonArray = new JSONArray("[" + aggValue + "]");
        JSONObject newValueJsonObject = new JSONObject(newValue);
        String newKey = newValueJsonObject.get(Constants.RX_NBR).toString() + "_" + newValueJsonObject.get(Constants.STORE_NBR).toString();
        HashMap<String, JSONObject> uniqueMap = new HashMap<>();

        //putting newValue in map
        loggingUtil.debug("concatenateWithDedup :: New value (" + newKey + ") - Adding in map", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        uniqueMap.put(newKey, newValueJsonObject);

        //iterating over every item in aggValueArray
        for (int i = 0; i < aggValueJsonArray.length(); i++) {
            JSONObject aggValueJsonObject = (JSONObject) aggValueJsonArray.get(i);
            String aggKey = aggValueJsonObject.get(Constants.RX_NBR).toString() + "_" + aggValueJsonObject.get(Constants.STORE_NBR).toString();

            //current aggValue not in map
            if(!uniqueMap.containsKey(aggKey)){
                loggingUtil.debug("concatenateWithDedup :: Fresh agg value (" + aggKey + ") - Adding in map", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                uniqueMap.put(aggKey, aggValueJsonObject);
            }
            //current aggValue present in map
            else{
                JSONObject existingRecord = uniqueMap.get(aggKey);

                //comparison with primary date comparison field
                if (getDateFromString(aggValueJsonObject.get(Constants.COMPARISON_DATE_FIELD_PRIMARY).toString())
                        .after(getDateFromString(existingRecord.get(Constants.COMPARISON_DATE_FIELD_PRIMARY).toString()))) {
                    loggingUtil.debug("concatenateWithDedup :: Duplicate agg value (" + aggKey + ") - Replacing in map - '" + Constants.COMPARISON_DATE_FIELD_PRIMARY + "' field is latest than existing", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                    uniqueMap.put(aggKey, aggValueJsonObject);
                }
                //comparison with secondary date comparison field since primary is missing
                else if (getDateFromString(aggValueJsonObject.get(Constants.COMPARISON_DATE_FIELD_SECONDARY).toString())
                        .after(getDateFromString(existingRecord.get(Constants.COMPARISON_DATE_FIELD_SECONDARY).toString()))) {
                    loggingUtil.debug("concatenateWithDedup :: Duplicate agg value (" + aggKey + ") - Replacing in map - '" + Constants.COMPARISON_DATE_FIELD_SECONDARY + "' field is latest than existing", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                    uniqueMap.put(aggKey, aggValueJsonObject);
                }
                else{
                    loggingUtil.debug("concatenateWithDedup :: Duplicate agg value (" + aggKey + ") - Not replacing in map - not latest than existing", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                }
            }
        }

        //making comma seperated json
        aggValue = StringUtils.removeStart(uniqueMap.values().toString(), "[");
        aggValue = StringUtils.removeEnd(aggValue, "]");
        return aggValue;
    }

    /**
     * @param existingJsonDate - input date string
     * @return - Date object
     * @throws ParseException       - throws when unable to parse
     * @throws InvalidDateException - throws when invalid date string
     */
    private Date getDateFromString(String existingJsonDate) throws ParseException, InvalidDateException {
        if (StringUtils.isBlank(existingJsonDate)) {
            throw new InvalidDateException("Invalid date String");
        }
        return new SimpleDateFormat(Constants.COMPARISON_DATE_FORMAT).parse(existingJsonDate);
    }

    /**
     * formatRecord - Formats the record as a JSON  array of 'immunization'
     *
     * @param k - Key
     * @param v - Value
     * @return keyValue - Map of PatId as key and 'immunization' array as value
     */
    public KeyValue<String, String> formatRecord(String k, String v, long batchLogSize) {
        KeyValue<String, String> keyValue = null;
        try {
            String finalData = "{ \"immunizations\" : [" + v + "] }";

            keyValue = new KeyValue<>(k, finalData);
            loggingUtil.debug("Successfully published the output event -   " + Constants.SUCCESSFULLY_HYDRATED_RECORD_FOR_PAT_ID + " : " + k,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);

            //batch logging counter increase upon successful publish
            RxImmunizationStreamer.batchCounter++;

            //batch logging when published desired number of messages
            if (RxImmunizationStreamer.batchCounter >= batchLogSize) {
                loggingUtil.info("Successfully published " + RxImmunizationStreamer.batchCounter + " output events", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                //resetting batch logging counter to log again only after desired count of messages got published
                RxImmunizationStreamer.batchCounter = 0;
            }

        } catch (Exception e) {
            loggingUtil.error(Constants.OUTPUT_TOPIC_PUBLISH_FAILED + Constants.SVC_ERROR + e,
                    Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        }
        return keyValue;
    }

}