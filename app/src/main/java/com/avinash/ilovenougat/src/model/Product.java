package com.avinash.ilovenougat.src.model;

/**
 * Created by Dell on 9/9/2016.
 */
public class Product {
    String brandName,thumbnailImageUrl,
        productId,
        originalPrice,
        styleId,
        colorId,
        price,
        percentOff,
        productUrl,
        productName;

    public Product(){};

    public Product(String brandName, String thumbnailImageUrl, String productId,
                   String originalPrice, String styleId, String colorId, String price,
                   String percentOff, String productUrl, String productName) {
        this.brandName = brandName;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.styleId = styleId;
        this.colorId = colorId;
        this.price = price;
        this.percentOff = percentOff;
        this.productUrl = productUrl;
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

/*"brandName":"Nike",
        "thumbnailImageUrl":"http:\/\/www.zappos.com\/images\/z\/3\/4\/1\/1\/7\/0\/3411705-t-THUMBNAIL.jpg",
        "productId":"8637230",
        "originalPrice":"$18.00",
        "styleId":"3411705",
        "colorId":"20476",
        "price":"$18.00",
        "percentOff":"0%",
        "productUrl":"http:\/\/www.zappos.com\/product\/8637230\/color\/20476",
        "productName":"Nike Stadium Football OTC"*/
