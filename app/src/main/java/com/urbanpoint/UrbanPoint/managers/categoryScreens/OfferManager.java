package com.urbanpoint.UrbanPoint.managers.categoryScreens;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Redeem;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.AddToWishlist;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.FoodOfferDeatils;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.Offer;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItems;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.PromoCodeStatus;
import com.urbanpoint.UrbanPoint.dataobject.wishList.WishListList;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.ADD_TO_WISHLIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_BEAUTY_OFFER_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FOOD_OFFER_DETAIL_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FOOD_OFFER_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID;

public class OfferManager implements CallBack {

    Context context;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    /**
     * Constructor
     *
     * @param contextObj                 The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public OfferManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    public void doGetOffers() {
        String jsonString = createOfferRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = GET_FOOD_OFFER_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERS_LIST, this, jsonString, tasksID);
    }

    public void doGetOffersDetails(String jsonParam) {
        String jsonString = jsonParam;
        commObj = new CommunicationManager(this.context);
        tasksID = GET_FOOD_OFFER_DETAIL_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERS_DETAIL, this, jsonString, tasksID);
    }


    public void doBeautyGetOffers(String jsonParam) {
        String jsonString = jsonParam;
        commObj = new CommunicationManager(this.context);
        tasksID = GET_BEAUTY_OFFER_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERS_LIST, this, jsonString, tasksID);
    }

    public void doFunActivityGetOffers(String jsonParam) {
        String jsonString = jsonParam;
        commObj = new CommunicationManager(this.context);
        tasksID = GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERS_LIST, this, jsonString, tasksID);
    }

    public void doHealthAndFitnessGetOffers(String jsonParam) {
        String jsonString = jsonParam;
        commObj = new CommunicationManager(this.context);
        tasksID = GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERS_LIST, this, jsonString, tasksID);
    }


    public void doAddToWishlist(String productID) {
        String productid = productID;
        String jsonString = createAddToWishlistRequestJSON("" + productid);
        commObj = new CommunicationManager(this.context);
        tasksID = ADD_TO_WISHLIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_ADD_TO_WISHLIST, this, jsonString, tasksID);
    }


    public void doCheckCustomerPin(String pin, String productID) {
        String jsonString = createCheckCustomerPinRequestJSON("" + pin, productID);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_CHECK_CUSTOMER_PIN_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CHECK_CUSTOMER_PIN, this, jsonString, tasksID);
    }

    public void doPurchaseConfirm(String pin, String productID) {
        String jsonString = createPurchaseConfirmRequestJSON("" + pin, productID);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_PURCHASE_CONFIRM_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_PURCHASE_CONFIRM, this, jsonString, tasksID);

    }

    public void doGetWishListList() {
        String jsonString = createWishListRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_WISHLIST_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_WISHLIST_LIST, this, jsonString, tasksID);
    }


    public void doGetOfferedPackages() {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_OFFERED_PACKAGES_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_OFFERED_PACKAGES_LIST, this, "", tasksID);
    }

    // To check promo code is valid or not.
    public void doCheckPromoCodesIsValid(String promoCode) {
        String jsonParam = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.PROMO_CODE, promoCode);
            jsonParam = jsonObject.toString();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CHECK_PROMO_CODE_IS_VALID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CHECK_PROMO_CODE_IS_VALID, this, jsonParam, tasksID);
    }

    /**
     * The interface method implemented in the java files
     *
     * @param data the result returned by the web service
     * @return none
     * @since 2014-08-28
     */
    @Override
    public void onResult(String data, int tasksID, String responseEmptyErrorMessage) {
        String errorMessage = "";
        JSONObject jsonObj;
        JSONObject responseDataObj;
        JSONObject responseObj;
        if (tasksID == GET_FOOD_OFFER_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Offer offer = gson.fromJson(data, Offer.class);
                    AppPreference.setSetting(context, "key_food_drink", data);
                    if (offer != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(offer.getStatus())) {
                            AppInstance.offers = null;
                            FlurryAgent.onError(GET_FOOD_OFFER_LIST_TASK_ID + "", offer.getMessage(), "");
                            FlurryAgent.logEvent(offer.getMessage());

                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            AppInstance.offers = offer;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(GET_FOOD_OFFER_LIST_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_food_drink", "");
                Offer strOffer = gson.fromJson(strData, Offer.class);
                if (strOffer != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strOffer.getStatus())) {
                        AppInstance.offers = null;
                        FlurryAgent.logEvent(strOffer.getMessage());
                        FlurryAgent.onError(GET_FOOD_OFFER_LIST_TASK_ID + "", strOffer.getMessage(), "");
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.offers = strOffer;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else {
                    FlurryAgent.logEvent(responseEmptyErrorMessage);
                    FlurryAgent.onError(GET_FOOD_OFFER_LIST_TASK_ID + "", responseEmptyErrorMessage, "");
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                }
            }

        } else if (tasksID == GET_FOOD_OFFER_DETAIL_TASK_ID) {
            if (data != null) {
                try {
                    FoodOfferDeatils foodOfferDeatils = new FoodOfferDeatils();
                    jsonObj = new JSONObject(data);
                    responseObj = jsonObj.getJSONObject("data");

                    String productID = responseObj.getString(Constants.OfferDetails.PRODUCT_ID);
                    String itemName = responseObj.getString(Constants.OfferDetails.ITEM_NAME);
                    String description = responseObj.getString(Constants.OfferDetails.DESCRIPTION);
                    String fineprint = responseObj.getString(Constants.OfferDetails.FINE_PRINT);
                    String expiry_date = responseObj.getString(Constants.OfferDetails.EXPIRY_DATE);
                    String merchant_id = responseObj.getString(Constants.OfferDetails.MERCHANT_ID);
                    String merchant_name = responseObj.getString(Constants.OfferDetails.MERCHANT_NAME);
                    String merchant_address = responseObj.getString(Constants.OfferDetails.MERCHANT_ADDRESS);
                    String merchant_image = responseObj.getString(Constants.OfferDetails.MERCHANT_IMAGE);
                    String timing = responseObj.getString(Constants.OfferDetails.TIMINGS);
                    String latitude = responseObj.getString(Constants.OfferDetails.LATITUDE);
                    String longitude = responseObj.getString(Constants.OfferDetails.LONGITUDE);
                    Log.d("LATI", "onDetail: " + latitude + " and longi " + longitude);
                    String phone = responseObj.getString(Constants.OfferDetails.PHONE);
                    String price = responseObj.getString(Constants.OfferDetails.PRICE);
                    String image = responseObj.getString(Constants.OfferDetails.IMAGE);
                    String special_price = responseObj.getString(Constants.OfferDetails.SPECIAL_PRICE);
                    String discount = responseObj.getString(Constants.OfferDetails.DISCOUNT);
                    String status = responseObj.getString(Constants.OfferDetails.STATUS);
                    String approxSaving = responseObj.getString(Constants.OfferDetails.APPROXSAVING);
                    String wishList_status = String.valueOf(responseObj.getInt(Constants.OfferDetails.WISHLIST_STATUS));

                    foodOfferDeatils.setProductId(productID);
                    foodOfferDeatils.setItemName(itemName);
                    foodOfferDeatils.setDescription(description);
                    foodOfferDeatils.setFineprint(fineprint);
                    foodOfferDeatils.setExpiredate(expiry_date);
                    foodOfferDeatils.setMerchantId(merchant_id);
                    foodOfferDeatils.setMerchantName(merchant_name);
                    foodOfferDeatils.setMerchantAddress(merchant_address);
                    foodOfferDeatils.setMerchantimage(merchant_image);
                    foodOfferDeatils.setTimings(timing);
                    foodOfferDeatils.setLatitude(latitude);
                    foodOfferDeatils.setLongitude(longitude);
                    foodOfferDeatils.setPhone(phone);
                    foodOfferDeatils.setPrice(price);
                    foodOfferDeatils.setImageURL(image);
                    foodOfferDeatils.setSpecialPrice(special_price);
                    foodOfferDeatils.setDiscount(discount);
                    foodOfferDeatils.setStatus(status);
                    foodOfferDeatils.setApproximateSavings(approxSaving);
                    foodOfferDeatils.setWishlist_item(wishList_status);


                    //  foodOfferDeatil.setItemName(data.);

                    if (foodOfferDeatils != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(foodOfferDeatils.getStatus())) {
                            AppInstance.foodOfferDeatils = null;
                        } else {
                            AppInstance.foodOfferDeatils = foodOfferDeatils;

                        }
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);

                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(GET_FOOD_OFFER_DETAIL_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }

            } else {
                FlurryAgent.logEvent(responseEmptyErrorMessage);
                FlurryAgent.onError(GET_FOOD_OFFER_DETAIL_TASK_ID + "", responseEmptyErrorMessage, "");
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == ADD_TO_WISHLIST_TASK_ID) {
            if (data != null) {
                try {
                    MyApplication.getInstance().printLogs("ADD_TO_WISHLIST_TASK_ID", data);
                    Gson gson = new GsonBuilder().create();
                    AppInstance.addToWishlist = gson.fromJson(data, AddToWishlist.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(ADD_TO_WISHLIST_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                FlurryAgent.logEvent(responseEmptyErrorMessage);
                FlurryAgent.onError(ADD_TO_WISHLIST_TASK_ID + "", responseEmptyErrorMessage, "");
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == GET_BEAUTY_OFFER_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Offer offer = gson.fromJson(data, Offer.class);
                    AppPreference.setSetting(context, "key_beauty_spa", data);
                    if (offer != null) {
                        if (offer.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.offers = null;
                            FlurryAgent.logEvent(offer.getMessage());
                            FlurryAgent.onError(GET_BEAUTY_OFFER_LIST_TASK_ID + "", offer.getMessage(), "");
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            AppInstance.offers = offer;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(GET_BEAUTY_OFFER_LIST_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }

            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_beauty_spa", "");
                Offer strOffer = gson.fromJson(strData, Offer.class);
                if (strOffer != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strOffer.getStatus())) {
                        AppInstance.offers = null;
                        FlurryAgent.logEvent(strOffer.getMessage());
                        FlurryAgent.onError(GET_BEAUTY_OFFER_LIST_TASK_ID + "", strOffer.getMessage(), "");
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.offers = strOffer;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else {
                    FlurryAgent.logEvent(responseEmptyErrorMessage);
                    FlurryAgent.onError(GET_BEAUTY_OFFER_LIST_TASK_ID + "", responseEmptyErrorMessage, "");
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                }
            }

        } else if (tasksID == GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Offer offer = gson.fromJson(data, Offer.class);
                    AppPreference.setSetting(context, "key_fun_activities", data);
                    if (offer != null) {
                        if (offer.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.offers = null;
                            FlurryAgent.logEvent(offer.getMessage());
                            FlurryAgent.onError(GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID + "", offer.getMessage(), "");
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            AppInstance.offers = offer;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_fun_activities", "");
                Offer strOffer = gson.fromJson(strData, Offer.class);
                if (strOffer != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strOffer.getStatus())) {
                        AppInstance.offers = null;
                        FlurryAgent.logEvent(strOffer.getMessage());
                        FlurryAgent.onError(GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID + "", strOffer.getMessage(), "");
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.offers = strOffer;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else {
                    FlurryAgent.logEvent(responseEmptyErrorMessage);
                    FlurryAgent.onError(GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID + "", responseEmptyErrorMessage, "");
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                }
            }
        } else if (tasksID == GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Offer offer = gson.fromJson(data, Offer.class);
                    AppPreference.setSetting(context, "key_health_fitness", data);
                    if (offer != null) {
                        if (offer.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.offers = null;
                            FlurryAgent.logEvent(offer.getMessage());
                            FlurryAgent.onError(GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID + "", offer.getMessage(), "");
                            serviceRedirectionObj.onFailureRedirection(offer.getMessage());
                        } else {
                            AppInstance.offers = offer;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_health_fitness", "");
                Offer strOffer = gson.fromJson(strData, Offer.class);
                if (strOffer != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strOffer.getStatus())) {
                        AppInstance.offers = null;
                        FlurryAgent.logEvent(strOffer.getMessage());
                        FlurryAgent.onError(GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID + "", strOffer.getMessage(), "");
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.offers = strOffer;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else {
                    FlurryAgent.logEvent(responseEmptyErrorMessage);
                    FlurryAgent.onError(GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID + "", responseEmptyErrorMessage, "");
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                }
            }
        } else if (tasksID == Constants.TaskID.GET_CHECK_CUSTOMER_PIN_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Redeem redeem = gson.fromJson(data, Redeem.class);
                    if (redeem != null) {
                        if (redeem.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            serviceRedirectionObj.onFailureRedirection(redeem.getMessage());
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == Constants.TaskID.GET_PURCHASE_CONFIRM_TASK_ID) {
            if (data != null) {
                Gson gson = new GsonBuilder().create();
                try {
                    Redeem redeemnew = gson.fromJson(data, Redeem.class);
                    if (redeemnew != null) {
                        if (redeemnew.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            serviceRedirectionObj.onFailureRedirection(redeemnew.getMessage());
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    } else {
                        serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
                    }
                } catch (Exception ex) {
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == Constants.TaskID.GET_WISHLIST_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    WishListList wishListList = gson.fromJson(data, WishListList.class);
                    AppPreference.setSetting(context, "key_fav_lst", data);
                    if (wishListList != null) {
                        if (wishListList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.wishListList = null;
                            serviceRedirectionObj.onFailureRedirection(wishListList.getMessage());
                        } else {
                            AppInstance.wishListList = wishListList;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    } else {
                        serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                try {
                    Gson gson = new GsonBuilder().create();
                    String strData = AppPreference.getSetting(context, "key_fav_lst", "");
                    WishListList wishListList = gson.fromJson(strData, WishListList.class);
                    if (wishListList != null) {
                        if (wishListList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.wishListList = null;
                            serviceRedirectionObj.onFailureRedirection(wishListList.getMessage());
                        } else {
                            AppInstance.wishListList = wishListList;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    } else {
                        serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }

        } else if (tasksID == Constants.TaskID.GET_OFFERED_PACKAGES_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    OfferPackagesItems packagesItems = gson.fromJson(data, OfferPackagesItems.class);
                    if (packagesItems != null) {
                        if (packagesItems.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.offerPackagesItems = null;
                            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
                        } else {
                            AppInstance.offerPackagesItems = packagesItems;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }

        } else if (tasksID == Constants.TaskID.CHECK_PROMO_CODE_IS_VALID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    PromoCodeStatus promoCodeDetails = gson.fromJson(data, PromoCodeStatus.class);
                    if (promoCodeDetails != null) {
                        if (promoCodeDetails.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.promoCodeStatus = null;
                            serviceRedirectionObj.onFailureRedirection(promoCodeDetails.getMessage());
                        } else {
                            AppInstance.promoCodeStatus = promoCodeDetails;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    } else {
                        serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }

        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }


    private String createOfferRequestJSON() {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CATEGORYID, "" + Constants.IntentPreference.FOOD_ID);
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            double latitude, longitude;

            // ----- FOR TESTING  END
            latitude = GPSTracker.getInstance(context).getLatitude();
            longitude = GPSTracker.getInstance(context).getLongitude();

//            latitude=25.3714297;
//            longitude=51.5474808;

            // ----- FOR TESTING  END
            MyApplication.getInstance().printLogs("latitude", "DATA : latitude : " + latitude);
            MyApplication.getInstance().printLogs("longitude", "DATA : longitude : " + longitude);
            MyApplication.getInstance().printLogs("GPS", "GPS DATA : isGPSEnabled : " + GPSTracker.getInstance(context).isGPSEnabled());
            MyApplication.getInstance().printLogs("GPS", "GPS DATA : isNetworkEnabled : " + GPSTracker.getInstance(context).isNetworkEnabled());

            Log.d("LATI", "OnJSoon: " + latitude + " and longi " + longitude);

            jsonObject.put(Constants.Request.LAT, latitude);
            jsonObject.put(Constants.Request.LONG, longitude);
            jsonObject.put(Constants.Request.PAGECOUNT, "1");

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createOfferRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    private String createCheckCustomerPinRequestJSON(String pin, String productID) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + productID);
            jsonObject.put(Constants.Request.PIN_CODE, "" + pin);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createCheckCustomerPinRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    private String createPurchaseConfirmRequestJSON(String pin, String productID) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + productID);
            jsonObject.put(Constants.Request.MERCHANT_PIN, "" + pin);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createPurchaseConfirmRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    public String createAddToWishlistRequestJSON(String productID) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + productID);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createAddWishListRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }


    private String createWishListRequestJSON() {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            double latitude, longitude;

            latitude = GPSTracker.getInstance(context).getLatitude();
            longitude = GPSTracker.getInstance(context).getLongitude();

            MyApplication.getInstance().printLogs("latitude", "DATA : latitude : " + latitude);
            MyApplication.getInstance().printLogs("longitude", "DATA : longitude : " + longitude);
            MyApplication.getInstance().printLogs("GPS", "GPS DATA : isGPSEnabled : " + GPSTracker.getInstance(context).isGPSEnabled());
            MyApplication.getInstance().printLogs("GPS", "GPS DATA : isNetworkEnabled : " + GPSTracker.getInstance(context).isNetworkEnabled());

            jsonObject.put(Constants.Request.LAT, latitude);
            jsonObject.put(Constants.Request.LONG, longitude);

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createWishlistRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}
