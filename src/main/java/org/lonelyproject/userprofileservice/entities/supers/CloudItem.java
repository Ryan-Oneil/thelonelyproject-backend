package org.lonelyproject.userprofileservice.entities.supers;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.lonelyproject.userprofileservice.entities.CloudItemDetails;

@MappedSuperclass
public class CloudItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cloud_item_detail_id")
    private CloudItemDetails itemDetails;

    @Column(nullable = false)
    private String url;

    public CloudItem() {
    }

    public CloudItem(CloudItemDetails itemDetails, String url) {
        this.itemDetails = itemDetails;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CloudItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(CloudItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
