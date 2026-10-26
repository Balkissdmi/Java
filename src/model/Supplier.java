package model;

import java.util.UUID;

public class Supplier {
    private UUID id = UUID.randomUUID();
    private String fullname;
    private String email;
    private String tel;
    private String vat;

    public Supplier() {}

    public Supplier(String fullname, String email, String tel, String vat) {
        this.fullname = fullname;
        this.email = email;
        this.tel = tel;
        this.vat = vat;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public String getVat() { return vat; }
    public void setVat(String vat) { this.vat = vat; }

    @Override
    public String toString() {
        return "Supplier{" + fullname + ", " + email + ", " + tel + ", VAT=" + vat + "}";
    }
}
