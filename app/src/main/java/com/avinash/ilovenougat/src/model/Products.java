package com.avinash.ilovenougat.src.model;

import java.util.List;

/**
 * Created by Dell on 9/9/2016.
 */
public class Products {

    String originalTerm,
            currentResultCount,
            totalResultCount,
            term,
            statusCode;
    List<Product> products;

    public Products(){}

    public Products(String originalTerm, String currentResultCount, String totalResultCount, String term, String statusCode, List<Product> products) {
        this.originalTerm = originalTerm;
        this.currentResultCount = currentResultCount;
        this.totalResultCount = totalResultCount;
        this.term = term;
        this.statusCode = statusCode;
        this.products = products;
    }

    public String getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(String originalTerm) {
        this.originalTerm = originalTerm;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

/*"originalTerm":"nike",
   "currentResultCount":"10",
   "totalResultCount":"4096",
   "term":"nike",
   "results",
   "statusCode":"200"
* */