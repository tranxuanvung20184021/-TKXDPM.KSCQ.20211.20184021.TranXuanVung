package controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.exception.InvalidDeliveryInfoException;
import common.exception.MediaNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;

public class PlaceRushOrderController extends BaseController{

    /**
     * Work to do:
     * placeRushOrder(cart: Cart, deliveryInfo: String, deliveryStruction: String, requireDate: date)
     */


    /**
     * For logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());


    /**
     * This method checks the avalibility for Rush Order of the products in the Cart
     * @throws SQLException
     */
    public void placeRushOrder(Date expectedDate,Date currDate, String address) throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct(); //--> 'Cart' object is static*
        if(!validateDate(expectedDate, currDate)) {
            throw new InvalidDeliveryInfoException("Chosen date is invalid");
        }
        if(!validateAddress(address)) {
            throw new InvalidDeliveryInfoException("Address does not support Rush Order");
        }
    }


    /**
     * This method processes purchase information
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processRushDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Rush Order Delivery Info");
        LOGGER.info(info.toString());
        validateRushDeliveryInfo(info);
    }


    /**
     * This method determines whether the date of receipt and order in Rush Order is valid or not
     * @param expectedDate
     * @param currDate
     * @throws InterruptedException
     * @throws IOException
     */
    public void processRushOrderDate(Date expectedDate, Date currDate) throws InterruptedException, IOException {
        LOGGER.info("Process Rush Order Delivery date");
        LOGGER.info("expected: "+ expectedDate.toString() + ",current:" + currDate.toString());
        validateDate(expectedDate, currDate);
    }


    /**
     * This method determines if the express shipping rush information is valid or not
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateRushDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        if(validatePhoneNumber(info.get("phone")) && validateName(info.get("name"))){}
        else throw new InvalidDeliveryInfoException("Some info is invalid");
    }


    /**
     * This method checks the validity of the phone number
     * @param phoneNumber
     * @return
     */
    public boolean validatePhoneNumber(String phoneNumber) { // Ki???m tra c??c ??i???u ki???n kh??ng h???p l??? c???a s??? ??i???n tho???i
        if(phoneNumber.length() != 10) return false; // tr?????ng h???p s??? ??i???n tho???i l???n h??n 10 s???
        if(phoneNumber.charAt(0) != '0') return false; // tr?????ng h???p s??? ??i???n tho???i kh??ng b???t ?????u b???ng 0
        try {
            Integer.parseInt(phoneNumber);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }


    /**
     * This method checks the validity of the name
     * @param name
     * @return
     */
    public boolean validateName(String name) { // Ki???m tra c??c tr?????ng h???p kh??ng h???p l??? c???a t??n - ch???a c??c k?? t??? ?????c bi???t
        if(name.length() == 0 || name.trim().length() == 0) return false; // tr?????ng h???p t??n r???ng
        for(int i=0; i<name.length(); i++){ // X??t chu???i t??n ????? check xem c?? nh???ng k?? t??? kh??ng h???p l??? hay kh??ng
            int a = name.charAt(i);
            if(a!=32){
                if((a<65 || (a>90 && a<97) || a>122)){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Check the address supports fast delivery or not
     * @param address
     * @return
     */
    public boolean validateAddress(String address) { // Ki???m tra ?????a ch??? nh???n h??ng c?? h??? tr??? giao h??ng nhanh hay kh??ng
        if(address.length() == 0 || address.trim().length() == 0) return false; // tr?????ng h???p ?????a ch??? r???ng
        String[] trueArray = {"hanoi", "ha noi", "haf noij", "hn", "hcm", "hochiminh", "hof chis minh", "h?? n???i", "h??? ch?? minh"};
        // m???ng ch???a t??n c??c t???nh h??? tr??? giao h??ng nhanh
        for(int i = 0; i < trueArray.length; i++) { // X??t ?????a ch??? giao h??ng xem c?? bao g???m nh???ng t???nh h??? tr??? giao h??ng nhanh hay kh??ng
            if(address.toLowerCase().endsWith(trueArray[i])){
                return true;
            }
        }
        return false;
    }


    /**
     * This method determine the validity of order date and receipt date
     * @param expectedDate
     * @param date
     * @return
     */
    public boolean validateDate(Date expectedDate, Date date) { // Ki???m tra ng??y nh???n h??ng v?? ng??y ?????t h??ng
        // c?? h???p l??? theo logic th???i gian hay kh??ng
        Date currDate = Calendar.getInstance().getTime();
        if (expectedDate.after(date)) return true;
        return false;
    }


    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
