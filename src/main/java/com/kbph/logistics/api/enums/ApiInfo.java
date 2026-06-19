package com.kbph.logistics.api.enums;

import com.kbph.logistics.api.constant.Constants;

public enum ApiInfo {
	API_MD_MAREMA("md_warehouse_dong", Constants.MASTER_PREFIX + "/marema"),
	API_MD_MLOCMA("md_location", Constants.MASTER_PREFIX + "/mlocma"),
	API_MD_MEQUMA("md_equipment", Constants.MASTER_PREFIX + "/mequma"),
	API_MD_MVHCMA("md_vehicle", Constants.MASTER_PREFIX + "/mvhcma"),
	API_MD_MCUSMA("md_customer", Constants.MASTER_PREFIX + "/mcusma"),
	API_MD_MDESMA("md_reg", Constants.MASTER_PREFIX + "/mdesma"),
	API_MD_MSKUWC("md_sku", Constants.MASTER_PREFIX + "/mskuwc"),

	API_IM_WASNIF("inbound_asn", Constants.INBOUND_PREFIX + "/wasnif"),
	API_IM_WASNIF_CANCEL("inbound_asn_cancel", Constants.INBOUND_PREFIX + "/wasnif/cancel"),
	API_IM_WRCVIT("inbound_order", Constants.INBOUND_PREFIX + "/wrcvit"),
	API_IM_WRCVIT_CANCEL("inbound_order_cancel", Constants.INBOUND_PREFIX + "/wrcvit/cancel"),
	API_IM_WRCVIT_CMP("inbound_order_complete", Constants.INBOUND_PREFIX + "/wrcvit/complete"),

	API_SM_WTAKIT("inventory_order", Constants.INVENTORY_PREFIX + "/wtakit"),
	API_SM_WTAKIT_UPDATE("inventory_order_update", Constants.INVENTORY_PREFIX + "/wtakit/update"),
	API_SM_WTAKIT_CANCEL("inventory_order_cancel", Constants.INVENTORY_PREFIX + "/wtakit/cancel"),
	API_SM_WPHYIT("inventory_count", Constants.INVENTORY_PREFIX + "/wphyit"),
	API_SM_WPHYIT_CREATE("inventory_create", Constants.INVENTORY_PREFIX + "/wphyit/create"),
	API_SM_WADJIT("inventory_adjustment", Constants.INVENTORY_PREFIX + "/adjust"),
	API_SM_BLOCK("inventory_block", Constants.INVENTORY_PREFIX + "/block"),
	API_SM_UNBLOCK("inventory_unblock", Constants.INVENTORY_PREFIX + "/unblock"),

	API_OM_WPLNHD("outbound_wplnhd", Constants.OUTBOUND_PREFIX + "/wplnhd"),
	API_OM_WPLNHD_CANCEL("outbound_wplnhd_cancel", Constants.OUTBOUND_PREFIX + "/wplnhd/cancel"),
	API_OM_WTAKIT("outbound_wtakit", Constants.OUTBOUND_PREFIX + "/wtakit"),
	API_OM_WTAKIT_CANCEL("outbound_wtakit_cancel", Constants.OUTBOUND_PREFIX + "/wtakit/cancel"),
	API_OM_WSHPIT("outbound_wshpit", Constants.OUTBOUND_PREFIX + "/wshpit");

    private final String apiCode;
    private final String url;

    ApiInfo(String apiCode, String url) {
        this.apiCode = apiCode;
        this.url = url;
    }

    public String getApiCode() {
        return apiCode;
    }

    public String getUrl() {
    	return url;
    }
}
