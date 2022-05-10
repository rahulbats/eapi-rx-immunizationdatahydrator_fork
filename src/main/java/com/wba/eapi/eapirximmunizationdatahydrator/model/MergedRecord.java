package com.wba.eapi.eapirximmunizationdatahydrator.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wba.eapi.eapirximmunizationdatahydrator.common.CommonUtils;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.AvroDefault;
import org.apache.avro.reflect.AvroName;
import org.apache.avro.reflect.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MergedRecord {

    @Nullable
    @AvroDefault(value="null")
    @AvroName("STORE_NBR")
    @JsonProperty("STORE_NBR")
    private String storeNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_NBR")
    @JsonProperty("RX_NBR")
    private String rxNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PAT_ID")
    @JsonProperty("PAT_ID")
    private String patientId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_ID")
    @JsonProperty("DRUG_ID")
    private Integer drugId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_NBR_LAST_DISP")
    @JsonProperty("FILL_NBR_LAST_DISP")
    private Integer fillNbrLastDispensed;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_NBR_PRESCRIBED")
    @JsonProperty("FILL_NBR_PRESCRIBED")
    private Integer fillNbrPrescribed;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_ORIGINAL_QTY")
    @JsonProperty("RX_ORIGINAL_QTY")
    private String rxOriginalQuantity;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_SIG")
    @JsonProperty("RX_SIG")
    private String rxSIG;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PBR_LAST_NAME")
    @JsonProperty("PBR_LAST_NAME")
    private String prescriberLastName;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PBR_ID")
    @JsonProperty("PBR_ID")
    private String prescriberId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_NON_SYSTEM_CD")
    @JsonProperty("DRUG_NON_SYSTEM_CD")
    private String drugNonSystemCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_90DAY_PREF_IND")
    @JsonProperty("RX_90DAY_PREF_IND")
    private String ninetyDayPrefInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_AUTO_IND")
    @JsonProperty("FILL_AUTO_IND")
    private String fillAutoInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_EXP_DTTM")
    @JsonProperty("RX_EXP_DTTM")
    private String rxExpDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_UNLIMITED_IND")
    @JsonProperty("FILL_UNLIMITED_IND")
    private String fillUnlimitedInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_ENTERED_DTTM")
    @JsonProperty("FILL_ENTERED_DTTM")
    private String fillEnteredDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_SOLD_DTTM")
    @JsonProperty("FILL_SOLD_DTTM")
    private String fillSoldDate;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_REFILLS_BY_DTTM")
    @JsonProperty("RX_REFILLS_BY_DTTM")
    private String rxRefillsByDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_CLASS")
    @JsonProperty("DRUG_CLASS")
    private String drugClass;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_DAW_IND")
    @JsonProperty("RX_DAW_IND")
    private String rxDawInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_IMMU_IND")
    @JsonProperty("RX_IMMU_IND")
    private String rxImmuInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_STATUS_CD")
    @JsonProperty("RX_STATUS_CD")
    private String rxStatusCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_TOTAL_DISPENSED_QTY")
    @JsonProperty("RX_TOTAL_DISPENSED_QTY")
    private String rxTotalQuantityDispensed;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PARTIAL_FIL_INTNDED_QTY")
    @JsonProperty("PARTIAL_FIL_INTNDED_QTY")
    private String partialFillIntendedQuantity;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("ORIGIN_CD")
    @JsonProperty("ORIGIN_CD")
    private String originCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("COB_PLAN_ID")
    @JsonProperty("COB_PLAN_ID")
    private String cobPlanId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_90DAY_PREF_DTTM")
    @JsonProperty("RX_90DAY_PREF_DTTM")
    private String ninetyDayPrefDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_90DAY_PREF_STAT_CD")
    @JsonProperty("RX_90DAY_PREF_STAT_CD")
    private String ninetyDayPrefStatusCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_90DAY_PREF_STAT_DTTM")
    @JsonProperty("RX_90DAY_PREF_STAT_DTTM")
    private String ninetyDayPrefStatusDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_MANUF_LOT_NBR")
    @JsonProperty("RX_VACC_MANUF_LOT_NBR")
    private String lotNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_EXP_DTTM")
    @JsonProperty("RX_VACC_EXP_DTTM")
    private String vaccExpiryDate;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_AREA_OF_ADMIN")
    @JsonProperty("RX_VACC_AREA_OF_ADMIN")
    private String site;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACCINE_OVERRIDE_IND")
    @JsonProperty("RX_VACCINE_OVERRIDE_IND")
    private String vaccOverrideInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_CONSENT_IND")
    @JsonProperty("RX_VACC_CONSENT_IND")
    private String vaccConsentInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_PNL_STATUS_CD")
    @JsonProperty("RX_VACC_PNL_STATUS_CD")
    private String vaccPnlStatusCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_VACC_PNL_ENT_DTTM")
    @JsonProperty("RX_VACC_PNL_ENT_DTTM")
    private String vaccPnlEntDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_CREATE_USER_ID")
    @JsonProperty("RX_CREATE_USER_ID ")
    private Integer rxCreateUserId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_CREATE_DTTM")
    @JsonProperty("RX_CREATE_DTTM")
    private String rxCreateDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_UPDATE_USER_ID")
    @JsonProperty("RX_UPDATE_USER_ID")
    private Integer rxUpdateUserId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_UPDATE_DTTM")
    @JsonProperty("RX_UPDATE_DTTM")
    private String rxUpdateDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_NBR")
    @JsonProperty("FILL_NBR")
    private Integer fillNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_PARTIAL_NBR")
    @JsonProperty("FILL_PARTIAL_NBR")
    private Integer partialFillNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_QTY_DISPENSED")
    @JsonProperty("FILL_QTY_DISPENSED")
    private String fillQuantityDispensed;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_TYPE_CD")
    @JsonProperty("FILL_TYPE_CD")
    private String fillTypeCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("WO_CORRELATION_ID")
    @JsonProperty("WO_CORRELATION_ID")
    private String woCorrelationId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_NBR_DISPENSED")
    @JsonProperty("FILL_NBR_DISPENSED")
    private Integer fillNbrDispensed;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_STATUS_CD")
    @JsonProperty("FILL_STATUS_CD")
    private String fillStatusCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_DAYS_SUPPLY")
    @JsonProperty("FILL_DAYS_SUPPLY")
    private Integer fillDaysSupply;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PARTIAL_FILL_CD")
    @JsonProperty("PARTIAL_FILL_CD")
    private String partialFillCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_LABEL_PRICE_AMT")
    @JsonProperty("FILL_LABEL_PRICE_AMT")
    private String fillLabelPriceAmt;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_RETAIL_PRICE_AMT")
    @JsonProperty("FILL_RETAIL_PRICE_AMT")
    private String fillRetailPriceAmt;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_PAY_METHOD_CD")
    @JsonProperty("FILL_PAY_METHOD_CD")
    private String fillPaymentMethodCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PLAN_ID")
    @JsonProperty("PLAN_ID")
    private String PlanId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_DATA_REV_DTTM")
    @JsonProperty("FILL_DATA_REV_DTTM")
    private String fillDataReviewDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_VERIFIED_DTTM")
    @JsonProperty("FILL_VERIFIED_DTTM")
    private String fillVerifiedDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_DELETED_DTTM")
    @JsonProperty("FILL_DELETED_DTTM")
    private String fillDeletedDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_ADJUDICATION_CD")
    @JsonProperty("FILL_ADJUDICATION_CD")
    private String fillAdjudicationCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_ADJUDICATION_DTTM")
    @JsonProperty("FILL_ADJUDICATION_DTTM")
    private String fillAdjudicationDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DL_REJECT_CD_01")
    @JsonProperty("DL_REJECT_CD_01")
    private String dlRejectCd1;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("BIN_NBR")
    @JsonProperty("BIN_NBR")
    @JsonAlias("DB_BIN_NBR")
    private String binNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PLAN_GROUP_NBR")
    @JsonProperty("PLAN_GROUP_NBR")
    @JsonAlias("DB_PLAN_GROUP_NBR")
    private String planGroupNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PROC_CTRL_NBR")
    @JsonProperty("PROC_CTRL_NBR")
    @JsonAlias("DB_PROC_CTRL_NBR")
    private String procCtrlNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("SHORT_FILL_IND")
    @JsonProperty("SHORT_FILL_IND")
    private String shortFillInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("WO_RX_COUNT")
    @JsonProperty("WO_RX_COUNT")
    private Integer woRxCount;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_DENIAL_OVERRIDE_CD")
    @JsonProperty("RX_DENIAL_OVERRIDE_CD")
    private String doseNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_DENIAL_OVERRIDE_CD_2")
    @JsonProperty("RX_DENIAL_OVERRIDE_CD_2 ")
    private String rxDenialOverrideCd2;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("RX_DENIAL_OVERRIDE_CD_3")
    @JsonProperty("RX_DENIAL_OVERRIDE_CD_3 ")
    private String rxDenialOverrideCd3;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_UPDATE_USER_ID")
    @JsonProperty("FILL_UPDATE_USER_ID")
    private Integer fillUpdateUserId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("FILL_UPDATE_DTTM")
    @JsonProperty("FILL_UPDATE_DTTM")
    private String fillUpdateDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("BILLING_NDC")
    @JsonProperty("BILLING_NDC")
    private String ndc;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PRODUCT_NAME_ABBR")
    @JsonProperty("PRODUCT_NAME_ABBR")
    private String productName;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("MFG_NAME")
    @JsonProperty("MFG_NAME")
    private String mfgName;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PACKAGE_SIZE")
    @JsonProperty("PACKAGE_SIZE")
    private String packageSize;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PACKAGE_SIZE_UOM")
    @JsonProperty("PACKAGE_SIZE_UOM")
    private String packageSizeUOM;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("PACKAGE_QTY")
    @JsonProperty("PACKAGE_QTY")
    private Integer packageQuantity;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_STRENGTH")
    @JsonProperty("DRUG_STRENGTH")
    private String drugStrength;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_STRENGTH_UOM")
    @JsonProperty("DRUG_STRENGTH_UOM")
    private String drugStrengthUOM;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DEFAULT_SIG")
    @JsonProperty("DEFAULT_SIG")
    private String defaultSIG;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("GEN_TYP_CD")
    @JsonProperty("GEN_TYP_CD")
    private String genTypeCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("SPECIALTY_DRUG_IND")
    @JsonProperty("SPECIALTY_DRUG_IND")
    private String specialtyDrugInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("CONCENTRATION_NBR")
    @JsonProperty("CONCENTRATION_NBR")
    private String concentrationNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("CONCENTRATION_UNITS")
    @JsonProperty("CONCENTRATION_UNITS")
    private String concentrationUnits;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("WIC_NBR")
    @JsonProperty("WIC_NBR")
    private Integer wicNumber;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("GPI")
    @JsonProperty("GPI")
    private String GPI;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("MED_GUIDE_IND")
    @JsonProperty("MED_GUIDE_IND")
    private String medGuideInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("MED_GUIDE_FILENAME")
    @JsonProperty("MED_GUIDE_FILENAME")
    private String medGuideFilename;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("MAINT_DRUG_IND")
    @JsonProperty("MAINT_DRUG_IND")
    private String maintDrugInd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("ROUTE_OF_ADMIN_CD")
    @JsonProperty("ROUTE_OF_ADMIN_CD")
    private String route;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_DOSAGE_FORM_CD")
    @JsonProperty("DRUG_DOSAGE_FORM_CD")
    private String drugDosageFormCd;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("THERA_CLASS")
    @JsonProperty("THERA_CLASS")
    private Integer theraClass;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_CREATE_USER_ID")
    @JsonProperty("DRUG_CREATE_USER_ID ")
    private Integer drugCreateUserId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_CREATE_DTTM")
    @JsonProperty("DRUG_CREATE_DTTM")
    private String drugCreateDttm;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_UPDATE_USER_ID")
    @JsonProperty("DRUG_UPDATE_USER_ID")
    private Integer drugUpdateUserId;

    @Nullable
    @AvroDefault(value="null")
    @AvroName("DRUG_UPDATE_DTTM")
    @JsonProperty("DRUG_UPDATE_DTTM")
    private String drugUpdateDttm;





    public GenericRecord getDrugId(){
        Schema schema = Schema.parse(CommonUtils.getSecretFromPath("src/main/resources/Drug_Key_Avro.avsc"));
        GenericRecord drugKey = new GenericData.Record(schema);
        drugKey.put("DRUG_ID",drugId);
        return drugKey;
    }

}