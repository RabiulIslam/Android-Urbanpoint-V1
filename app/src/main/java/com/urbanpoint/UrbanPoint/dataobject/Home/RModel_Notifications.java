package com.urbanpoint.UrbanPoint.dataobject.Home;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/16/2017.
 */

public class RModel_Notifications {

    @SerializedName("status-success")
    private boolean status_success;

    public boolean getStatusSuccess() {
        return this.status_success;
    }

    public void setStatusSuccess(boolean status_success) {
        this.status_success = status_success;
    }

    @SerializedName("status-code")

    private int status_code;

    public int getStatusCode() {
        return this.status_code;
    }

    public void setStatusCode(int status_code) {
        this.status_code = status_code;
    }

    @SerializedName("status-text")
    private String status_text;

    public String getStatusText() {
        return this.status_text;
    }

    public void setStatusText(String status_text) {
        this.status_text = status_text;
    }

    private Data data;

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Item {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        @SerializedName("title")
        private String title;

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title1) {
            this.title = title1;
        }

        private String push;

        public String getPush() {
            return this.push;
        }

        public void setPush(String push) {
            this.push = push;
        }

        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        private String readed;

        public String getReaded() {
            return this.readed;
        }

        public void setReaded(String readed) {
            this.readed = readed;
        }
    }

    public class Self {
        private String href;

        public String getHref() {
            return this.href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

    public class Links {
        private Self self;

        public Self getSelf() {
            return this.self;
        }

        public void setSelf(Self self) {
            this.self = self;
        }
    }

    public class Pagination {
        private int totalCount;

        public int getTotalCount() {
            return this.totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        private int pageCount;

        public int getPageCount() {
            return this.pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        private int currentPage;

        public int getCurrentPage() {
            return this.currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        private int perPage;

        public int getPerPage() {
            return this.perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }
    }

    public class Data {
        private ArrayList<Item> items;

        public ArrayList<Item> getItems() {
            return this.items;
        }

        public void setItems(ArrayList<Item> items) {
            this.items = items;
        }

        private Links links;

        public Links getLinks() {
            return this.links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

        private Pagination pagination;

        public Pagination getPagination() {
            return this.pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }


}
