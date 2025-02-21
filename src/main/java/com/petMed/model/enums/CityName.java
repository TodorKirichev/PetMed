package com.petMed.model.enums;

public enum CityName {
    BLAGOEVGRAD("Blagoevgrad"),
    BURGAS("Burgas"),
    VARNA("Varna"),
    VIDIN("Vidin"),
    VELIKO_TARNOVO("Veliko Tarnovo"),
    GABROVO("Gabrovo"),
    DOBRICH("Dobrich"),
    KARDZHALI("Kardzhali"),
    KYUSTENDIL("Kyustendil"),
    LOVECH("Lovech"),
    MONTANA("Montana"),
    PAZARDZHIK("Pazardzhik"),
    PERNIK("Pernik"),
    PLEVEN("Pleven"),
    PLOVDIV("Plovdiv"),
    RAZGRAD("Razgrad"),
    RUSE("Ruse"),
    SILISTRA("Silistra"),
    SLIVEN("Sliven"),
    SMOLYAN("Smolyan"),
    SOFIA("Sofia"),
    STARA_ZAGORA("Stara Zagora"),
    TARGOVISHTE("Targovishte"),
    HASKOVO("Haskovo"),
    SHUMEN("Shumen"),
    YAMBOL("Yambol");

    private final String cityName;

    CityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
