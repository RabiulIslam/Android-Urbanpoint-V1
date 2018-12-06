package com.urbanpoint.UrbanPoint.dataobject.Home;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/16/2017.
 */

public class RModel_FavoritesList {
    private String status;

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    private ArrayList<Datum> data;

    public ArrayList<Datum> getData() { return this.data; }

    public void setData(ArrayList<Datum> data) { this.data = data; }

    private int flag;

    public int getFlag() { return this.flag; }

    public void setFlag(int flag) { this.flag = flag; }

    private String vodafoneCustomerStatus;

    public String getVodafoneCustomerStatus() { return this.vodafoneCustomerStatus; }

    public void setVodafoneCustomerStatus(String vodafoneCustomerStatus) { this.vodafoneCustomerStatus = vodafoneCustomerStatus; }

    private String ooredooCustomerStatus;

    public String getOoredooCustomerStatus() { return this.ooredooCustomerStatus; }

    public void setOoredooCustomerStatus(String ooredooCustomerStatus) { this.ooredooCustomerStatus = ooredooCustomerStatus; }

    public class Datum
    {
        private String productid;

        public String getProductid() { return this.productid; }

        public void setProductid(String productid) { this.productid = productid; }

        private String categoryid;

        public String getCategoryid() { return this.categoryid; }

        public void setCategoryid(String categoryid) { this.categoryid = categoryid; }

        private String productname;

        public String getProductname() { return this.productname; }

        public void setProductname(String productname) { this.productname = productname; }

        private String isEnabled;

        public String getIsEnabled() { return this.isEnabled; }

        public void setIsEnabled(String isEnabled) { this.isEnabled = isEnabled; }

        private String price;

        public String getPrice() { return this.price; }

        public void setPrice(String price) { this.price = price; }

        private String merchantid;

        public String getMerchantid() { return this.merchantid; }

        public void setMerchantid(String merchantid) { this.merchantid = merchantid; }

        private String merchantname;

        public String getMerchantname() { return this.merchantname; }

        public void setMerchantname(String merchantname) { this.merchantname = merchantname; }

        private String merchantaddress;

        public String getMerchantaddress() { return this.merchantaddress; }

        public void setMerchantaddress(String merchantaddress) { this.merchantaddress = merchantaddress; }

        private String specialprice;

        public String getSpecialprice() { return this.specialprice; }

        public void setSpecialprice(String specialprice) { this.specialprice = specialprice; }

        private int Discount;

        public int getDiscount() { return this.Discount; }

        public void setDiscount(int Discount) { this.Discount = Discount; }

        private String image;

        public String getImage() { return this.image; }

        public void setImage(String image) { this.image = image; }

        private String product_count;

        public String getProductCount() { return this.product_count; }

        public void setProductCount(String product_count) { this.product_count = product_count; }

        private String Expired;

        public String getExpired() { return this.Expired; }

        public void setExpired(String Expired) { this.Expired = Expired; }

        private String prep;

        public String getPrep() { return this.prep; }

        public void setPrep(String prep) { this.prep = prep; }

        private int distance;

        public int getDistance() { return this.distance; }

        public void setDistance(int distance) { this.distance = distance; }
    }


}
