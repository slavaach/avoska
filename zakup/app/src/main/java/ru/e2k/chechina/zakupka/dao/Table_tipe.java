package ru.e2k.chechina.zakupka.dao;


//интерфейс для всех типов из базы

public interface Table_tipe {
    public Table_tipe get();//сама запись
    public String getText(int i);//строка поля по номеру столбца
    public boolean isSimpel();//для товаров -постоянный или временный. В остальных не используется
    public int getId();//Id записи
    public void setBzakupOnli(boolean bzakup);//установка галки закупили
    public int[] R_id_view(); //список полей для отражения в grid-е
    public String getTextView(int i);//выдает строку по R.id поля
    public boolean check();//заказанили выделен
    public void setCheck(boolean bzakup);//установка выделенной

}
