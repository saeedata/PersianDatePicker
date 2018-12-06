package com.PersianCalendar.Component;

import java.beans.Customizer;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.table.DefaultTableModel;

public class PersianDateChooser extends JPanel implements Customizer {

    Object bean;
    private PersianDateTableModel p;
    private DefaultTableModel model;
    private int[] xy = new int[2];
    private PersianCalendar pcal = new PersianCalendar();
    private int year;
    private int month;
    private int day;
    private String dayName;

    private int presentYear;
    private int presentMonth;
    private int presentDay;
    private String presentDayName;

    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JComboBox jComboBox1;
    private JScrollPane jScrollPane2;
    private JSpinner jSpinner2;
    private JTable jTable2;
    private JButton jButton1;
    private JLabel jLabel1;

    @Override
    public void setObject(Object bean) {
        this.bean = bean;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        jTable2.addMouseListener(l);
        jButton1.addMouseListener(l);
        jComboBox1.addMouseListener(l);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
        jSpinner2.setValue(year);
    }

    public void setMonth(int month) {
        this.month = month;
        jComboBox1.setSelectedIndex(month - 1);
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDayName(String dayName) {
        for (String day : PersianCalendar.SHAMSI_DAYS) {
            if (dayName.equals(day)) {
                this.dayName = dayName;
                return;
            }
        }
    }

    public String getDayName() {
        return dayName;
    }

    public PersianDateChooser() {

        this.setPreferredSize(new Dimension(300, 176));
        this.setMinimumSize(new Dimension(300, 176));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        year = presentYear = pcal.getYear();
        month = presentMonth = pcal.getMonth();
        day = presentDay = pcal.getDay();
        dayName = presentDayName = pcal.getDayName();

        jPanel1 = new JPanel();
        jPanel1.setLayout(new GridLayout(1, 0, 10, 0));

        jComboBox1 = new JComboBox();
        jComboBox1.setLightWeightPopupEnabled(false);
        jComboBox1.setModel(new DefaultComboBoxModel(PersianCalendar.SHAMSI_MONTHS));
        jComboBox1.setSelectedIndex(presentMonth - 1);
        jComboBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                jTable2.setModel(p.getPersianDate(Integer.valueOf(jSpinner2.getValue().toString()), jComboBox1.getSelectedIndex() + 1, 0, xy, model));
            }
        });
        jPanel1.add(jComboBox1);

        jSpinner2 = new JSpinner();
        jSpinner2.setEditor(new NumberEditor(jSpinner2, "0"));
        jSpinner2.setValue(presentYear);
        jSpinner2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {

                jTable2.setModel(p.getPersianDate(Integer.valueOf(jSpinner2.getValue().toString()), jComboBox1.getSelectedIndex() + 1, 0, xy, model));
            }
        });
        jPanel1.add(jSpinner2);

        this.add(jPanel1);

        model = new DefaultTableModel() {

            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

        };
        model.addColumn("شنبه");
        model.addColumn("یک");
        model.addColumn("دو");
        model.addColumn("سه");
        model.addColumn("چهار");
        model.addColumn("پنج");
        model.addColumn("جمعه");
        jTable2 = new JTable();
        jTable2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        jTable2.setCellSelectionEnabled(true);
        jTable2.setShowHorizontalLines(false);
        jTable2.setShowVerticalLines(false);
        p = new PersianDateTableModel();
        jTable2.setModel(p.getPersianDate(presentYear, presentMonth, presentDay, xy, model));
        jTable2.changeSelection(xy[0], xy[1], false, false);
        jTable2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectRow = jTable2.getSelectedRow();
                int selectCol = jTable2.getSelectedColumn();
                Object obj = null;
                if (selectRow >= 0 && selectRow < 6 && selectCol >= 0 && selectCol < 7) {
                    obj = jTable2.getValueAt(selectRow, selectCol);
                }
                if (obj != null) {
                    day = Integer.valueOf(obj.toString());
                    month = jComboBox1.getSelectedIndex() + 1;
                    year = Integer.valueOf(jSpinner2.getValue().toString());
                    int d = selectCol - 1 < 0 ? 6 : selectCol - 1;
                    dayName = PersianCalendar.SHAMSI_DAYS[d];
                    jLabel1.setText(dayName + "  -  " + year + "/" + month + "/" + day + "     ");
                }
            }
        });

        jScrollPane2 = new JScrollPane();
        jScrollPane2.setViewportView(jTable2);

        jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0));
        jPanel2.add(jScrollPane2);

        this.add(jPanel2);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        jPanel3 = new JPanel();
        jPanel3.setLayout(new GridBagLayout());
        jButton1 = new JButton("امروز");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jSpinner2.setValue(presentYear);
                jComboBox1.setSelectedIndex(presentMonth - 1);
                jTable2.changeSelection(xy[0], xy[1], false, false);
                jLabel1.setText(presentDayName + "  -  " + presentYear + "/" + presentMonth + "/" + presentDay + "     ");
//                year = presentYear;
//                month = presentMonth;
//                day = presentDay;
//                dayName = presentDayName;
            }
        });
        jPanel3.add(jButton1, gridBagConstraints);

        jLabel1 = new JLabel(presentDayName + "  -  " + presentYear + "/" + presentMonth + "/" + presentDay + "     ");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabel1, gridBagConstraints);
        this.add(jPanel3);
    }

    class PersianDateTableModel {

        public DefaultTableModel getPersianDate(int year, int month, int day, int[] xy, DefaultTableModel model) {

            deleteAllRows(model);
            model.setRowCount(6);
            int startDay = PersianCalendar.getStartDay(year, month) + 1;

            int numberOfDaysInMonth = PersianCalendar.getNumberOfMonthDays(year, month);

            for (int i = 0; i < startDay; i++) {
                model.setValueAt(null, 0, i);
            }
            int j = 0;
            if (startDay == 7) {
                startDay = 0;
            }
            for (int i = 1; i <= numberOfDaysInMonth; i++) {
                if (day != 0 && i == day) {
                    xy[0] = j;
                    xy[1] = startDay;
                }
                model.setValueAt(i, j, startDay);
                startDay++;
                if (startDay == 7) {
                    j++;
                    startDay = 0;
                }
            }

            return model;
        }

        public void deleteAllRows(DefaultTableModel model) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }
}
