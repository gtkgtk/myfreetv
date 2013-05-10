package org.rom.myfreetv.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

class DateSpinner extends JSpinner {

    public static int DATE_ONLY = 1;
    public static int TIME_ONLY = 2;

    // public DateSpinner(Date current, Date min, Date max, String format, int
    // larg) {
    // super(new SpinnerDateModel(current,min,max,Calendar.MINUTE));
    // JSpinner.DateEditor editor = new JSpinner.DateEditor(this,format);
    // JTextField jtf = editor.getTextField();
    // jtf.setColumns(larg);
    // jtf.setHorizontalAlignment(JTextField.CENTER);
    // setEditor(editor);
    // }

    // public DateSpinner(Date current, Date min, Date max) {
    // this(current,min,max,"dd-MM-yyyy HH:mm",11);
    // }

    public DateSpinner(int mode) {
        super();
        switch(mode) {
            case 1:
                Date min = new GregorianCalendar(2006, 0, 1).getTime();
                Date max = new GregorianCalendar(2099, 11, 31, 23, 59, 59).getTime();
                init(Calendar.getInstance().getTime(), min, max, "dd-MM-yyyy", 10);
                break;
            case 2:
                init(Calendar.getInstance().getTime(), null, null, "HH:mm", 5);
                break;
            default:
                throw new IllegalArgumentException("Le mode doit être DATE_ONLY ou HOUR_ONLY.");
        }
    }

    private void init(Date current, Date min, Date max, String format, int larg) {
        setModel(new SpinnerDateModel(current, min, max, Calendar.MINUTE));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(this, format);
        JTextField jtf = editor.getTextField();
        jtf.setColumns(larg);
        jtf.setHorizontalAlignment(JTextField.CENTER);
        setEditor(editor);
    }

    public Calendar getCalendar() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(((SpinnerDateModel) getModel()).getDate().getTime());
        return cal;
    }

    public void setValue(Date date) {
        ((SpinnerDateModel) getModel()).setValue(date);
    }

}