package com.ManShirtShop.common.genCode;

public class GenCode {
    public static String code(Integer id) {
        if (id < 1000000000) {
            if (id < 100000000) {
                if (id < 10000000) {
                    if (id < 1000000) {
                        if (id < 100000) {
                            if (id < 10000) {
                                if (id < 1000) {
                                    if (id < 100) {
                                        if (id < 10) {
                                            return "000000000" + id;
                                        }
                                        return "00000000" + id;
                                    }
                                    return "0000000" + id;
                                }
                                return "000000" + id;
                            }
                            return "00000" + id;
                        }
                        return "0000" + id;
                    }
                    return "000" + id;
                }
                return "00" + id;
            }
            return "0" + id;
        }
        return "" + id;

    }

    public static String codeProduct(Integer id) {
        if (id < 100000) {
            if (id < 10000) {
                if (id < 1000) {
                    if (id < 100) {
                        if (id < 10) {
                            return "00000" + id;
                        }
                        return "0000" + id;
                    }
                    return "000" + id;
                }
                return "00" + id;
            }
            return "0" + id;
        }
        return "" + id;

    }
}
