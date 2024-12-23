package org.lonelyproject.userprofileservice.entities.supers;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.lonelyproject.userprofileservice.entities.CloudItemDetails;

@MappedSuperclass
public class MediaCloudItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_cloud_item_detail_id")
    private CloudItemDetails original;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blur_cloud_item_detail_id")
    private CloudItemDetails blur;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "optimised_cloud_item_detail_id")
    private CloudItemDetails optimised;

    public MediaCloudItem() {
    }

    public MediaCloudItem(CloudItemDetails original) {
        this.original = original;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CloudItemDetails getOriginal() {
        return original;
    }

    public void setOriginal(CloudItemDetails original) {
        this.original = original;
    }

    public CloudItemDetails getBlur() {
        return blur;
    }

    public void setBlur(CloudItemDetails blur) {
        this.blur = blur;
    }

    public CloudItemDetails getOptimised() {
        return optimised;
    }

    public void setOptimised(CloudItemDetails optimised) {
        this.optimised = optimised;
    }
}
