package com.aet.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

public class Constants {
    public static String json = "json";
    public static String PUT = "PUT";
    public static String DELETE = "DELETE";
//    public static String DOMAIN = "http://10.0.2.2";
    public static String BASE_URL = "http://karyaptiuty.id/";
    public static String STORAGE_PATH = "http://karyaptiuty.id/storage/";
    public static String CHANNEL_ID = "mychannelid";
    public static String CHANNEL_NAME = "mychannelname";
    public static String CHANNEL_DESCRIPTION = "my description";
    public static String MULTIPLE_CHOICE = "multiple_choice";
    public static String TRUE_FALSE = "true_or_false";
    public static String ESSAY = "essay";
    public static String AUTHORIZATION = "key=AAAA77zIZJQ:APA91bGDdU4kXZT0tMHXg-0mNm4y4XU8hnxcSsMJJ2BYKIi8lqCrK4O8AYwj2yxP229jIjKuEpCuaIx0qPRmsKFDmLiCByTFcaW8jYJMrd5mkRkPTHGk1qQ9UpOM2ZtyCQVejlaN_Od3";
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static Cell createCell(String content, int colspan, int rowspan, int border) {
        Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content));
        cell.setBorder(null);
        if (8 == (border & 8)) {
            cell.setBorderRight(new SolidBorder(1));

            cell.setBorderBottom(new SolidBorder(1));
        }
        if (4 == (border & 4)) {
            cell.setBorderLeft(new SolidBorder(1));
        }
        if (2 == (border & 2)) {
            cell.setBorderBottom(new SolidBorder(1));
        }
        if (1 == (border & 1)) {
            cell.setBorderTop(new SolidBorder(1));
        }
        return cell;

    }
}
