package com.urbanpoint.UrbanPoint.dataobject.Home;

/**
 * Created by Lenovo on 8/12/2017.
 */

public class RModel_CheckSubscription {
    private String status;

    private String first_weak;

    private String first_month;

    private String new_customer;

    private String first_week_rem_days;

    private String subscription_page;

    private String message;

    private String home_screen_message;

    private int un_readed_badge_count;

    private int un_readed_notifications_count;

    private String uber_status;

    public RModel_CheckSubscription() {
        this.status = "";
        this.first_weak = "";
        this.first_month = "";
        this.new_customer = "";
        this.first_week_rem_days = "";
        this.subscription_page = "";
        this.message = "";
        this.home_screen_message = "";
        this.un_readed_badge_count = 0;
        this.uber_status ="";
        this.un_readed_notifications_count = 0;
        this.data = new Data();
    }


    public String getUberStatus() { return this.uber_status; }

    public void setUberStatus(String uber_status) { this.uber_status = uber_status; }



    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getFirst_weak() {
        return first_weak;
    }

    public void setFirst_weak(String first_weak) {
        this.first_weak = first_weak;
    }

    public String getSubscription_page() {
        return subscription_page;
    }

    public void setSubscription_page(String subscription_page) {
        this.subscription_page = subscription_page;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHome_screen_message() {
        return home_screen_message;
    }

    public String getFirst_week_rem_days() {
        return first_week_rem_days;
    }

    public void setFirst_week_rem_days(String first_week_rem_days) {
        this.first_week_rem_days = first_week_rem_days;
    }

    public void setHome_screen_message(String home_screen_message) {
        this.home_screen_message = home_screen_message;
    }

    public String getFirst_month() {
        return first_month;
    }

    public void setFirst_month(String first_month) {
        this.first_month = first_month;
    }

    public int getUn_readed_badge_count() {
        return un_readed_badge_count;
    }

    public void setUn_readed_badge_count(int un_readed_badge_count) {
        this.un_readed_badge_count = un_readed_badge_count;
    }

    public int getUn_readed_notifications_count() {
        return un_readed_notifications_count;
    }

    public void setUn_readed_notifications_count(int un_readed_notifications_count) {
        this.un_readed_notifications_count = un_readed_notifications_count;
    }

    public String getNew_customer() {
        return new_customer;
    }

    public void setNew_customer(String new_customer) {
        this.new_customer = new_customer;
    }

    private Data data;

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        public Data() {
            this.id = "";
            this.customer_id = "";
            this.msisdn = "";
            this.operator = "";
            this.start_datetime = "";
            this.expiry_datetime = "";
            this.status = "";
            this.newuser = "";
            this.access_code_id = "";
            this.created_at = "";
            this.updated_at = "";
        }

        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String customer_id;

        public String getCustomerId() {
            return this.customer_id;
        }

        public void setCustomerId(String customer_id) {
            this.customer_id = customer_id;
        }

        private String msisdn;

        public String getMsisdn() {
            return this.msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        private String operator;

        public String getOperator() {
            return this.operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        private String start_datetime;

        public String getStartDatetime() {
            return this.start_datetime;
        }

        public void setStartDatetime(String start_datetime) {
            this.start_datetime = start_datetime;
        }

        private String expiry_datetime;

        public String getExpiryDatetime() {
            return this.expiry_datetime;
        }

        public void setExpiryDatetime(String expiry_datetime) {
            this.expiry_datetime = expiry_datetime;
        }

        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String newuser;

        public String getNewuser() {
            return this.newuser;
        }

        public void setNewuser(String newuser) {
            this.newuser = newuser;
        }

        private String access_code_id;

        public String getAccessCodeId() {
            return this.access_code_id;
        }

        public void setAccessCodeId(String access_code_id) {
            this.access_code_id = access_code_id;
        }

        private String created_at;

        public String getCreatedAt() {
            return this.created_at;
        }

        public void setCreatedAt(String created_at) {
            this.created_at = created_at;
        }

        private String updated_at;

        public String getUpdatedAt() {
            return this.updated_at;
        }

        public void setUpdatedAt(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    private Version version;

    public Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public class Version {
        private String version;

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        private String forcefully_updated;

        public String getForcefullyUpdated() {
            return this.forcefully_updated;
        }

        public void setForcefullyUpdated(String forcefully_updated) {
            this.forcefully_updated = forcefully_updated;
        }
    }
}
