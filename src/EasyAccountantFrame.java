import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by andre on 3/24/2017.
 */
public class EasyAccountantFrame extends JFrame {
    JPanel[] panel = new JPanel[8];

    final List<Break> holidays = new ArrayList<>();

    public static final String eulaText = "<html>" +
            "<p>This product has been created for sole use by Alexander Lavin by the expressed consent of its creator, Andrew Baker.<p>" +
            "<p>This product can be shown to anyone by Alexander Lavin or its creator.<p>" +
            "<p>All other uses of this product are expressly prohibited.<p>" +
            "<p>By pressing accept, the user agrees that they are Alexander Lavin and will not allow use of this product by any other user.<p>" +
            "</html>";

    public EasyAccountantFrame () { }

    public void eula() {
        new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setVisible(true);
        Box container = Box.createVerticalBox();

        panel[0] = new JPanel();
        JLabel eula = new JLabel();
        eula.setText(eulaText);
        panel[0].add(eula);
        panel[0].setSize((int)panel[0].getPreferredSize().getWidth(), (int)panel[0].getPreferredSize().getHeight());

        container.add(panel[0]);

        panel[1] = new JPanel();
        JButton acceptButton = new JButton("accept");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(container);
                makeFrame();
            }
        });
        panel[1].add(acceptButton);
        panel[1].setSize((int)panel[1].getPreferredSize().getWidth(), (int)panel[1].getPreferredSize().getHeight());

        container.add(panel[1]);
        this.add(container);
        setSize();
    }

    public void makeFrame() {
        new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setVisible(true);
        Box container = Box.createVerticalBox();

        panel[0] = new JPanel();
        JTextArea blank = new JTextArea(0,0);
        blank.setVisible(false);
        panel[0].add(blank);
        blank.requestFocus();

        String defaultText = "Total contract value";
        JTextField moneyTextField = new JTextField(20);
        panel[0].add(moneyTextField);
        addDefaultText(moneyTextField, "Total contract value");
        moneyTextField.setSize(100, 100);
//        moneyTextField.setVisible(false);
        container.add(panel[0]);

        panel[1] = new JPanel();
        JLabel dateText = new JLabel("Start and end dates: ");
        panel[1].add(dateText);
        new BoxLayout(panel[1], BoxLayout.X_AXIS);
        final JDatePicker startDatePicker = newJDatePickerImpl();
        panel[1].add((Component) startDatePicker);
        final JDatePicker endDatePicker = newJDatePickerImpl();
        panel[1].add((Component) endDatePicker);
        container.add(panel[1]);

        panel[2] = new JPanel();
        JLabel holidayText = new JLabel("Start and end holidays: ");
        panel[2].add(holidayText);
        new BoxLayout(panel[2], BoxLayout.X_AXIS);
        final JDatePicker startHolidayPicker = newJDatePickerImpl();
        panel[2].add((Component) startHolidayPicker);
        final JDatePicker endHolidayPicker = newJDatePickerImpl();
        panel[2].add((Component) endHolidayPicker);
        container.add(panel[2]);

        panel[3] = new JPanel();
        JLabel holidaysPanel = new JLabel();
        panel[3].add(holidaysPanel);
        holidaysPanel.setText("Hi");
        holidaysPanel.setText("");
        container.add(panel[3]);

        panel[4] = new JPanel();
        JButton addButton = new JButton("add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar startHoliday = Calendar.getInstance();
                startHoliday.setTime((Date)startHolidayPicker.getModel().getValue());
                Calendar endHoliday = Calendar.getInstance();
                endHoliday.setTime((Date)endHolidayPicker.getModel().getValue());
                if (CalendarComparator.compare(startHoliday, endHoliday) <= 0) {
                    Break holiday = new Break(startHoliday, endHoliday);

                    holidays.add(holiday);
                    StringBuilder stringBuilder = new StringBuilder("<html>");
                    for (Break breaks : holidays) {
                        stringBuilder.append( "<p>" + breaks.toString() + "</p>");
                    }
                    stringBuilder.append("</html>");
                    holidaysPanel.setText(stringBuilder.toString());
//                    holidaysPanel.setPreferredSize(new Dimension((int) Math.ceil(holidaysPanel.getPreferredSize().getWidth()), textLinePreferredSize *holidays.size()));
                    holidaysPanel.setSize((int)holidaysPanel.getPreferredSize().getWidth(), (int)holidaysPanel.getPreferredSize().getHeight());
                    setSize();
                }
            }
        });

        JButton removeButton = new JButton("remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                holidays.remove(holidays.size() - 1);
                StringBuilder stringBuilder = new StringBuilder("<html>");
                for (Break breaks : holidays) {
                    stringBuilder.append( "<p>" + breaks.toString() + "</p>");
                }
                stringBuilder.append("</html>");
                holidaysPanel.setText(stringBuilder.toString());
                holidaysPanel.setSize((int)holidaysPanel.getPreferredSize().getWidth(), (int)holidaysPanel.getPreferredSize().getHeight());
                setSize();
            }
        });

        JButton removeAllButton = new JButton("remove all");
        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                holidays.removeAll(holidays);
                holidaysPanel.setText("");
                holidaysPanel.setSize((int)holidaysPanel.getPreferredSize().getWidth(), (int)holidaysPanel.getPreferredSize().getHeight());
                setSize();
            }
        });

        panel[4].add(addButton);
        panel[4].add(removeButton);
        panel[4].add(removeAllButton);
        container.add(panel[4]);

        panel[5] = new JPanel();
        JLabel cheapPayText = new JLabel("End of writer's guild pay: ");
        panel[5].add(cheapPayText);
        new BoxLayout(panel[5], BoxLayout.X_AXIS);
        JDatePicker endLowPay = newJDatePickerImpl();
        panel[5].add((Component) endLowPay);

        JTextField writersGuildTextField = new JTextField(20);
        panel[5].add(writersGuildTextField);
        addDefaultText(writersGuildTextField, "Writer's guild weekly pay rate");

        container.add(panel[5]);

        panel[6] = new JPanel();

        JLabel solutionLabel = new JLabel();

        JButton okButton = new JButton("submit");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int contractValue = (int) (Double.valueOf(moneyTextField.getText()) * 100);
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(((Date) startDatePicker.getModel().getValue()));
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(((Date) endDatePicker.getModel().getValue()));
                    Calendar endLowPayDate = Calendar.getInstance();
                    endLowPayDate.setTime(((Date) endLowPay.getModel().getValue()));
                    int writersGuildPay = (int) (Double.valueOf(writersGuildTextField.getText()) * 100 / 5);

                    String text = Application.getInfo(startDate, endDate, contractValue, holidays, endLowPayDate, writersGuildPay);
                    solutionLabel.setText(text);
//                    solutionLabel.setPreferredSize(new Dimension((int) Math.ceil(solutionLabel.getPreferredSize().getWidth()), solutionLabel));
                    setSize();
                    solutionLabel.setSize(solutionLabel.getWidth(), solutionLabel.getHeight());
                } catch (Exception ex) {

                }
            }
        });
        panel[6].add(okButton);
        container.add(panel[6]);

        panel[7] = new JPanel();
        panel[7].add(solutionLabel);
        container.add(panel[7]);

        setSize();
        this.add(container);
        this.validate();

//        this.pack();
    }

    private void addDefaultText(JTextField textField, String defaultText) {
        textField.setText(defaultText);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(defaultText)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().equals("")) {
                    textField.setText(defaultText);
                }
            }
        });
    }

    public JDatePickerImpl newJDatePickerImpl() {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        return datePicker;
    }


    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "dd.MM.yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    private void setSize() {
        int height = 0;
        int width = 0;
        for (int i=0; i<panel.length; i++) {
            if (panel[i] != null) {
                if (width < panel[i].getPreferredSize().getWidth()) {
                    width = (int) Math.ceil(panel[i].getPreferredSize().getWidth());
                }
                height += (int) Math.ceil(panel[i].getPreferredSize().getHeight());
                height += 20;
            }
        }
        this.setSize(width + 30, height);
    }
}
