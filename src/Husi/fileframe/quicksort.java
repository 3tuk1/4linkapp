package Husi.fileframe;

import javax.swing.*;

public class quicksort {
    static boolean up;
    public static void setUporDown(boolean up){
        quicksort.up = up;
    }
    static void quickSort(DefaultListModel<String> filemo, int left, int right,int sort) {
        // 再帰処理の終了条件を配列の先頭のインデックス=left が配列の最後のインデックス=right-1 以上に設定
        // →分割した配列の要素数が1個以下になったら再帰終了
        if (left >= right - 1) {
            fileFrame.setFileList(filemo);
            return;
        }
        // right-1のデータリストを参照してその番号のダブル値を取得する
        String pivot_str = filemo.getElementAt(right-1);
        double pivot = Double.parseDouble(filemo.getElementAt(right-1).split("[,:]")[sort]);
        // pivotIndex を 配列の先頭のインデックス=left で初期化
        int pivotIndex = left;
        for (int i = left; i < right - 1; i++) {
            // もし a[i] が pivot より小さいなら
            if(up){
                if (Double.parseDouble(filemo.getElementAt(i).split("[,:]")[sort]) > pivot) {
                    // a[pivotIndex] と a[i] を交換
                    String temp = filemo.getElementAt(pivotIndex);
                    filemo.set(pivotIndex, filemo.getElementAt(i));
                    filemo.set(i, temp);
                    // pivotIndex を 1 だけ増やす
                    pivotIndex++;
                }

            }else {
                if (Double.parseDouble(filemo.getElementAt(i).split("[,:]")[sort]) < pivot) {
                    // a[pivotIndex] と a[i] を交換
                    String temp = filemo.getElementAt(pivotIndex);
                    filemo.set(pivotIndex, filemo.getElementAt(i));
                    filemo.set(i, temp);
                    // pivotIndex を 1 だけ増やす
                    pivotIndex++;
                }
            }
        }

        // ピボットと a[pivotIndex] を交換
        filemo.set(right-1,filemo.getElementAt(pivotIndex));
        filemo.set(pivotIndex,pivot_str);

        // quickSort(a, left, pivotIndex) を呼び出す
        quickSort(filemo, left, pivotIndex,sort);
        // quickSort(a, pivotIndex+1, right) を呼び出す
        quickSort(filemo, pivotIndex+1, right,sort);
    }
}
