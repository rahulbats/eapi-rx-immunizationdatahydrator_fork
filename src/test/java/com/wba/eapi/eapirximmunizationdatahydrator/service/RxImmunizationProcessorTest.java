package com.wba.eapi.eapirximmunizationdatahydrator.service;

import com.wba.eapi.eapirximmunizationdatahydrator.common.LoggingUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.exception.InvalidDateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RxImmunizationProcessorTest {

    @InjectMocks
    RxImmunizationProcessor rxImmunizationProcessor;

    @Mock
    LoggingUtils loggingUtil;

    @Test
    public void concatenateWithDedupe() {
        String aggValue = "{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"exist\",\"RX_UPDATE_DTTM\":\"2015-10-09 16:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}";
        aggValue += ",{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"exist\",\"RX_UPDATE_DTTM\":\"2015-10-22 16:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}";
        List<String> listNewValue = new ArrayList<>();
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"unique\",\"RX_UPDATE_DTTM\":\"2015-10-22 16:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"exist\",\"RX_UPDATE_DTTM\":\"2015-10-11 17:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"rx_dttm_before\",\"RX_UPDATE_DTTM\":\"2015-10-09 16:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"rx_dttm_before\",\"RX_UPDATE_DTTM\":\"2015-10-22 15:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"fill_dttm_diff\",\"RX_UPDATE_DTTM\":\"2015-10-09 15:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-22 19:01:39\"}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"fill_dttm_diff\",\"RX_UPDATE_DTTM\":\"2015-10-09 15:01:39\",\"FILL_UPDATE_DTTM\":\"2015-10-09 16:01:39\"}");

        for (String newValue : listNewValue) {
            try {
                aggValue = rxImmunizationProcessor.concatenateWithDedup(aggValue, newValue);
            } catch (ParseException | InvalidDateException e) {
                e.printStackTrace();
            }
        }

        //todo: remove sout
        System.out.println("AggValue Final: " + aggValue);
    }

    @Test
    public void concatenateWithDedupeNullHandling() {
        String aggValue = "{\"RX_NBR\":\"no_store\",\"PAT_ID\":\"51348000548\",\"DRUG_ID\":665461,\"FILL_NBR_LAST_DISP\":1,\"FILL_NBR_PRESCRIBED\":1,\"RX_ORIGINAL_QTY\":\"0.500\",\"RX_SIG\":\"m\\u00ed\\u00f0\\u0105\\u010c\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\u0100\\u010e\\u00dd\",\"PBR_LAST_NAME\":null,\"PBR_ID\":\"50308000113\",\"DRUG_NON_SYSTEM_CD\":\"N\",\"RX_90DAY_PREF_IND\":null,\"FILL_AUTO_IND\":null,\"RX_EXP_DTTM\":\"2016-10-09 00:00:00\",\"FILL_UNLIMITED_IND\":\"N\",\"FILL_ENTERED_DTTM\":\"2015-10-09 16:01:21\",\"FILL_SOLD_DTTM\":\"2015-10-09 16:01:39\",\"RX_REFILLS_BY_DTTM\":\"2016-10-08 00:00:00\",\"DRUG_CLASS\":\"RX\",\"RX_DAW_IND\":\"N\",\"RX_IMMU_IND\":\"Y\",\"RX_STATUS_CD\":\"AC\",\"RX_TOTAL_DISPENSED_QTY\":\"0.500\",\"PARTIAL_FIL_INTNDED_QTY\":null,\"ORIGIN_CD\":null,\"COB_PLAN_ID\":null,\"RX_90DAY_PREF_DTTM\":null,\"RX_90DAY_PREF_STAT_CD\":null,\"RX_90DAY_PREF_STAT_DTTM\":null,\"RX_VACC_MANUF_LOT_NBR\":\"234\",\"RX_VACC_EXP_DTTM\":\"2009-09-09 12:00:00\",\"RX_VACC_AREA_OF_ADMIN\":\"L\",\"RX_VACCINE_OVERRIDE_IND\":\"Y\",\"RX_VACC_CONSENT_IND\":null,\"RX_VACC_PNL_STATUS_CD\":\"0\",\"RX_VACC_PNL_ENT_DTTM\":\"2015-10-12 12:28:46\",\"RX_CREATE_USER_ID\":\"\\u0101\\u00f3\\u0146\\u00da\\u01646\\u00dbL\\u00fa\",\"RX_CREATE_DTTM\":\"2015-10-09 16:01:21\",\"RX_UPDATE_USER_ID\":\"\\u00d3\\u0166\\u00fa\\u010a\\u00e5\",\"RX_UPDATE_DTTM\":\"2015-10-01 16:01:39\",\"FILL_NBR\":1,\"FILL_PARTIAL_NBR\":0,\"FILL_QTY_DISPENSED\":\"0.300\",\"FILL_TYPE_CD\":\"N\",\"WO_CORRELATION_ID\":null,\"FILL_NBR_DISPENSED\":1,\"FILL_STATUS_CD\":\"SD\",\"FILL_DAYS_SUPPLY\":1,\"PARTIAL_FILL_CD\":null,\"FILL_LABEL_PRICE_AMT\":\"5.00\",\"FILL_RETAIL_PRICE_AMT\":\"8.99\",\"FILL_PAY_METHOD_CD\":\"T\",\"PLAN_ID\":\"ILBC\",\"FILL_DATA_REV_DTTM\":\"2022-03-08 01:18:28\",\"FILL_VERIFIED_DTTM\":\"2022-03-08 01:20:34\",\"FILL_DELETED_DTTM\":null,\"FILL_ADJUDICATION_CD\":\"A\",\"FILL_ADJUDICATION_DTTM\":\"2022-03-08 01:15:24\",\"DL_REJECT_CD_01\":null,\"BIN_NBR\":\"014468\",\"PLAN_GROUP_NBR\":\"D0PAYABLE\",\"PROC_CTRL_NBR\":\"COVIDIMZ  \",\"SHORT_FILL_IND\":null,\"WO_RX_COUNT\":null,\"RX_DENIAL_OVERRIDE_CD\":\"02\",\"RX_DENIAL_OVERRIDE_CD_2\":null,\"RX_DENIAL_OVERRIDE_CD_3\":null,\"FILL_UPDATE_USER_ID\":\"\\u00a2\\u014c\\u0152\\u012d\\u012e\",\"FILL_UPDATE_DTTM\":\"2022-03-08 01:22:00\",\"BILLING_NDC\":null,\"PRODUCT_NAME_ABBR\":null,\"MFG_NAME\":null,\"PACKAGE_SIZE\":null,\"PACKAGE_SIZE_UOM\":null,\"PACKAGE_QTY\":null,\"DRUG_STRENGTH\":null,\"DRUG_STRENGTH_UOM\":null,\"DEFAULT_SIG\":null,\"GEN_TYP_CD\":null,\"SPECIALTY_DRUG_IND\":null,\"CONCENTRATION_NBR\":null,\"CONCENTRATION_UNITS\":null,\"WIC_NBR\":null,\"GPI\":null,\"MED_GUIDE_IND\":null,\"MED_GUIDE_FILENAME\":null,\"MAINT_DRUG_IND\":null,\"ROUTE_OF_ADMIN_CD\":null,\"DRUG_DOSAGE_FORM_CD\":null,\"THERA_CLASS\":null,\"DRUG_CREATE_USER_ID\":null,\"DRUG_CREATE_DTTM\":null,\"DRUG_UPDATE_USER_ID\":null,\"DRUG_UPDATE_DTTM\":null}";
        List<String> listNewValue = new ArrayList<>();
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"fill_dttm_diff\",\"PAT_ID\":\"51348000548\",\"DRUG_ID\":665461,\"FILL_NBR_LAST_DISP\":1,\"FILL_NBR_PRESCRIBED\":1,\"RX_ORIGINAL_QTY\":\"0.500\",\"RX_SIG\":\"m\\u00ed\\u00f0\\u0105\\u010c\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\u0100\\u010e\\u00dd\",\"PBR_LAST_NAME\":null,\"PBR_ID\":\"50308000113\",\"DRUG_NON_SYSTEM_CD\":\"N\",\"RX_90DAY_PREF_IND\":null,\"FILL_AUTO_IND\":null,\"RX_EXP_DTTM\":\"2016-10-09 00:00:00\",\"FILL_UNLIMITED_IND\":\"N\",\"FILL_ENTERED_DTTM\":\"2015-10-09 16:01:21\",\"FILL_SOLD_DTTM\":\"2015-10-09 16:01:39\",\"RX_REFILLS_BY_DTTM\":\"2016-10-08 00:00:00\",\"DRUG_CLASS\":\"RX\",\"RX_DAW_IND\":\"N\",\"RX_IMMU_IND\":\"Y\",\"RX_STATUS_CD\":\"AC\",\"RX_TOTAL_DISPENSED_QTY\":\"0.500\",\"PARTIAL_FIL_INTNDED_QTY\":null,\"ORIGIN_CD\":null,\"COB_PLAN_ID\":null,\"RX_90DAY_PREF_DTTM\":null,\"RX_90DAY_PREF_STAT_CD\":null,\"RX_90DAY_PREF_STAT_DTTM\":null,\"RX_VACC_MANUF_LOT_NBR\":\"234\",\"RX_VACC_EXP_DTTM\":\"2009-09-09 12:00:00\",\"RX_VACC_AREA_OF_ADMIN\":\"L\",\"RX_VACCINE_OVERRIDE_IND\":\"Y\",\"RX_VACC_CONSENT_IND\":null,\"RX_VACC_PNL_STATUS_CD\":\"0\",\"RX_VACC_PNL_ENT_DTTM\":\"2015-10-12 12:28:46\",\"RX_CREATE_USER_ID\":\"\\u0101\\u00f3\\u0146\\u00da\\u01646\\u00dbL\\u00fa\",\"RX_CREATE_DTTM\":\"2015-10-09 16:01:21\",\"RX_UPDATE_USER_ID\":\"\\u00d3\\u0166\\u00fa\\u010a\\u00e5\",\"RX_UPDATE_DTTM\":\"2015-10-09 16:01:39\",\"FILL_NBR\":1,\"FILL_PARTIAL_NBR\":0,\"FILL_QTY_DISPENSED\":\"0.300\",\"FILL_TYPE_CD\":\"N\",\"WO_CORRELATION_ID\":null,\"FILL_NBR_DISPENSED\":1,\"FILL_STATUS_CD\":\"SD\",\"FILL_DAYS_SUPPLY\":1,\"PARTIAL_FILL_CD\":null,\"FILL_LABEL_PRICE_AMT\":\"5.00\",\"FILL_RETAIL_PRICE_AMT\":\"8.99\",\"FILL_PAY_METHOD_CD\":\"T\",\"PLAN_ID\":\"ILBC\",\"FILL_DATA_REV_DTTM\":\"2022-03-08 01:18:28\",\"FILL_VERIFIED_DTTM\":\"2022-03-08 01:20:34\",\"FILL_DELETED_DTTM\":null,\"FILL_ADJUDICATION_CD\":\"A\",\"FILL_ADJUDICATION_DTTM\":\"2022-03-08 01:15:24\",\"DL_REJECT_CD_01\":null,\"BIN_NBR\":\"014468\",\"PLAN_GROUP_NBR\":\"D0PAYABLE\",\"PROC_CTRL_NBR\":\"COVIDIMZ  \",\"SHORT_FILL_IND\":null,\"WO_RX_COUNT\":null,\"RX_DENIAL_OVERRIDE_CD\":\"02\",\"RX_DENIAL_OVERRIDE_CD_2\":null,\"RX_DENIAL_OVERRIDE_CD_3\":null,\"FILL_UPDATE_USER_ID\":\"\\u00a2\\u014c\\u0152\\u012d\\u012e\",\"FILL_UPDATE_DTTM\":\"\",\"BILLING_NDC\":null,\"PRODUCT_NAME_ABBR\":null,\"MFG_NAME\":null,\"PACKAGE_SIZE\":null,\"PACKAGE_SIZE_UOM\":null,\"PACKAGE_QTY\":null,\"DRUG_STRENGTH\":null,\"DRUG_STRENGTH_UOM\":null,\"DEFAULT_SIG\":null,\"GEN_TYP_CD\":null,\"SPECIALTY_DRUG_IND\":null,\"CONCENTRATION_NBR\":null,\"CONCENTRATION_UNITS\":null,\"WIC_NBR\":null,\"GPI\":null,\"MED_GUIDE_IND\":null,\"MED_GUIDE_FILENAME\":null,\"MAINT_DRUG_IND\":null,\"ROUTE_OF_ADMIN_CD\":null,\"DRUG_DOSAGE_FORM_CD\":null,\"THERA_CLASS\":null,\"DRUG_CREATE_USER_ID\":null,\"DRUG_CREATE_DTTM\":null,\"DRUG_UPDATE_USER_ID\":null,\"DRUG_UPDATE_DTTM\":null}");
        listNewValue.add("{\"STORE_NBR\":\"59383\",\"RX_NBR\":\"fill_dttm_diff\",\"PAT_ID\":\"51348000548\",\"DRUG_ID\":665461,\"FILL_NBR_LAST_DISP\":1,\"FILL_NBR_PRESCRIBED\":1,\"RX_ORIGINAL_QTY\":\"0.500\",\"RX_SIG\":\"m\\u00ed\\u00f0\\u0105\\u010c\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\u0100\\u010e\\u00dd\",\"PBR_LAST_NAME\":null,\"PBR_ID\":\"50308000113\",\"DRUG_NON_SYSTEM_CD\":\"N\",\"RX_90DAY_PREF_IND\":null,\"FILL_AUTO_IND\":null,\"RX_EXP_DTTM\":\"2016-10-09 00:00:00\",\"FILL_UNLIMITED_IND\":\"N\",\"FILL_ENTERED_DTTM\":\"2015-10-09 16:01:21\",\"FILL_SOLD_DTTM\":\"2015-10-09 16:01:39\",\"RX_REFILLS_BY_DTTM\":\"2016-10-08 00:00:00\",\"DRUG_CLASS\":\"RX\",\"RX_DAW_IND\":\"N\",\"RX_IMMU_IND\":\"Y\",\"RX_STATUS_CD\":\"AC\",\"RX_TOTAL_DISPENSED_QTY\":\"0.500\",\"PARTIAL_FIL_INTNDED_QTY\":null,\"ORIGIN_CD\":null,\"COB_PLAN_ID\":null,\"RX_90DAY_PREF_DTTM\":null,\"RX_90DAY_PREF_STAT_CD\":null,\"RX_90DAY_PREF_STAT_DTTM\":null,\"RX_VACC_MANUF_LOT_NBR\":\"234\",\"RX_VACC_EXP_DTTM\":\"2009-09-09 12:00:00\",\"RX_VACC_AREA_OF_ADMIN\":\"L\",\"RX_VACCINE_OVERRIDE_IND\":\"Y\",\"RX_VACC_CONSENT_IND\":null,\"RX_VACC_PNL_STATUS_CD\":\"0\",\"RX_VACC_PNL_ENT_DTTM\":\"2015-10-12 12:28:46\",\"RX_CREATE_USER_ID\":\"\\u0101\\u00f3\\u0146\\u00da\\u01646\\u00dbL\\u00fa\",\"RX_CREATE_DTTM\":\"2015-10-09 16:01:21\",\"RX_UPDATE_USER_ID\":\"\\u00d3\\u0166\\u00fa\\u010a\\u00e5\",\"RX_UPDATE_DTTM\":\"2015-10-09 16:01:39\",\"FILL_NBR\":1,\"FILL_PARTIAL_NBR\":0,\"FILL_QTY_DISPENSED\":\"0.300\",\"FILL_TYPE_CD\":\"N\",\"WO_CORRELATION_ID\":null,\"FILL_NBR_DISPENSED\":1,\"FILL_STATUS_CD\":\"SD\",\"FILL_DAYS_SUPPLY\":1,\"PARTIAL_FILL_CD\":null,\"FILL_LABEL_PRICE_AMT\":\"5.00\",\"FILL_RETAIL_PRICE_AMT\":\"8.99\",\"FILL_PAY_METHOD_CD\":\"T\",\"PLAN_ID\":\"ILBC\",\"FILL_DATA_REV_DTTM\":\"2022-03-08 01:18:28\",\"FILL_VERIFIED_DTTM\":\"2022-03-08 01:20:34\",\"FILL_DELETED_DTTM\":null,\"FILL_ADJUDICATION_CD\":\"A\",\"FILL_ADJUDICATION_DTTM\":\"2022-03-08 01:15:24\",\"DL_REJECT_CD_01\":null,\"BIN_NBR\":\"014468\",\"PLAN_GROUP_NBR\":\"D0PAYABLE\",\"PROC_CTRL_NBR\":\"COVIDIMZ  \",\"SHORT_FILL_IND\":null,\"WO_RX_COUNT\":null,\"RX_DENIAL_OVERRIDE_CD\":\"02\",\"RX_DENIAL_OVERRIDE_CD_2\":null,\"RX_DENIAL_OVERRIDE_CD_3\":null,\"FILL_UPDATE_USER_ID\":\"\\u00a2\\u014c\\u0152\\u012d\\u012e\",\"FILL_UPDATE_DTTM\":\"2022-03-08 01:22:00\",\"BILLING_NDC\":null,\"PRODUCT_NAME_ABBR\":null,\"MFG_NAME\":null,\"PACKAGE_SIZE\":null,\"PACKAGE_SIZE_UOM\":null,\"PACKAGE_QTY\":null,\"DRUG_STRENGTH\":null,\"DRUG_STRENGTH_UOM\":null,\"DEFAULT_SIG\":null,\"GEN_TYP_CD\":null,\"SPECIALTY_DRUG_IND\":null,\"CONCENTRATION_NBR\":null,\"CONCENTRATION_UNITS\":null,\"WIC_NBR\":null,\"GPI\":null,\"MED_GUIDE_IND\":null,\"MED_GUIDE_FILENAME\":null,\"MAINT_DRUG_IND\":null,\"ROUTE_OF_ADMIN_CD\":null,\"DRUG_DOSAGE_FORM_CD\":null,\"THERA_CLASS\":null,\"DRUG_CREATE_USER_ID\":null,\"DRUG_CREATE_DTTM\":null,\"DRUG_UPDATE_USER_ID\":null,\"DRUG_UPDATE_DTTM\":null}");

        for (String newValue : listNewValue) {
            try {
                aggValue = rxImmunizationProcessor.concatenateWithDedup(aggValue, newValue);
            } catch (ParseException | InvalidDateException e) {
                e.printStackTrace();
            }
        }

        System.out.println("AggValue Final: " + aggValue);
    }
}