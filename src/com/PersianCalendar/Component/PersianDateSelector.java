package com.PersianCalendar.Component;

import java.awt.*;
import java.awt.event.*;
import java.beans.Customizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PersianDateSelector extends JPanel implements Customizer {

    private Object bean;
    private int year;
    private int month;
    private int day;
    private String dayName;
    private PersianDateChooser p;
    private JDialog j;
    private JButton jButton1;
    private JTextField jTextField1;

    public PersianDateSelector() {
        jTextField1 = new JTextField();
        jButton1 = new JButton();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        jTextField1.setMaximumSize(new Dimension(483647, 32));
        jTextField1.setMinimumSize(new Dimension(32, 32));
        jTextField1.setPreferredSize(new Dimension(120, 32));
        jTextField1.setEditable(false);
        this.add(jTextField1);

        jButton1.setIcon(new ImageIcon(getClass().getResource("/Calendar32.png"))); // NOI18N
        jButton1.setMaximumSize(new Dimension(32, 32));
        jButton1.setMinimumSize(new Dimension(32, 32));
        jButton1.setPreferredSize(new Dimension(32, 32));
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                p.setYear(p.getYear());
                p.setMonth(p.getMonth());
                p.setDay(p.getDay());
                p.setDayName(p.getDayName());
                j.setLocation((int) jButton1.getLocationOnScreen().getX() - 270, (int) jButton1.getLocationOnScreen().getY() + 33);
                j.setVisible(true);
            }
        });
        this.add(jButton1);
        p = new PersianDateChooser();
        j = new JDialog();
        j.setResizable(false);
        j.setUndecorated(true);
        j.setContentPane(p);
        j.setMinimumSize(new Dimension(300, 176));
        j.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {

                j.dispose();
            }

        });

        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                year = p.getYear();
                month = p.getMonth();
                day = p.getDay();
                jTextField1.setText(year + "/" + month + "/" + day);
            }

        });

        year = p.getYear();
        month = p.getMonth();
        day = p.getDay();
        dayName = p.getDayName();

        jTextField1.setText(year + "/" + month + "/" + day);
    }

    public void setObject(Object bean) {
        this.bean = bean;
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
        if (year > 1300 && year < 1500) {
            this.year = year;
            p.setYear(year);
            jTextField1.setText(year + "/" + month + "/" + day);

        }
    }

    public void setMonth(int month) {
        if (month > 0 && month < 13) {
            this.month = month;
            p.setMonth(month);
            jTextField1.setText(year + "/" + month + "/" + day);
        }
    }

    public void setDay(int day) {
        int dayAvailable = PersianCalendar.getNumberOfMonthDays(year, month);
        if (day > 0 && day <= dayAvailable) {
            this.day = day;
            p.setDay(day);
            dayName = "";
            jTextField1.setText(year + "/" + month + "/" + day);
        } else {
            try {
                throw new Exception("Day is incorrect");
            } catch (Exception ex) {
                Logger.getLogger(PersianDateSelector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getDayName() {
        return dayName;
    }

    public void clearDate() {
        year = 0;
        month = 0;
        day = 0;
        dayName = "";
        jTextField1.setText("");
    }

}
