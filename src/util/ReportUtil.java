package util;

import db.Koneksi;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;

public class ReportUtil {

    public static void laporanPenjualan() {
        try {
            Connection conn = Koneksi.getConnection();

            JasperReport jr = JasperCompileManager.compileReport("report/laporan_penjualan.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}