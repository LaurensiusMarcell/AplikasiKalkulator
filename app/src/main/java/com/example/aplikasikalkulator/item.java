package com.example.aplikasikalkulator;

public class item {
    private Integer Input1;
    private Integer Input2;
    private String operasi;
    private Integer hasil;

    public item(int In1, int In2, String op, int hsl) {
        Input1 = In1;
        Input2 = In2;
        operasi = op;
        hasil = hsl;

    }

    public String keString(){
        return Input1.toString()+" "+operasi+" "+Input2.toString() +" = "+hasil.toString();
    }


}
