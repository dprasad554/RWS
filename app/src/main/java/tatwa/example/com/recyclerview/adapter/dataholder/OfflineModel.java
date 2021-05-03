package tatwa.example.com.recyclerview.adapter.dataholder;

/**
 * Created by tatwa on 10/9/2018.
 */

public class OfflineModel {

    public OfflineModel(String landmark, String tubewellid) {
        this.landmark = landmark;
        this.tubewellid = tubewellid;
    }

    public OfflineModel() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String userId;
    public String complainId;
    public String status;
    public String latitude;
    public String longitude;
    public String imageFile;
    public String actionDate;
    public String remarks;

    public String getTubewellid() {
        return tubewellid;
    }

    public void setTubewellid(String tubewellid) {
        this.tubewellid = tubewellid;
    }

    public String landmark;
    public String tubewellid;

    public String getCategory() {
        return category;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String category;
    public String days;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String ticketNo;

}
