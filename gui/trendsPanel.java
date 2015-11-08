package gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import tag_trends.Test;
import tag_trends.parser.Country;

public class trendsPanel extends JPanel implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    static UtilDateModel modelFrom;
	static UtilDateModel modelTo;
    static JComboBox<String> locationList;
private static JButton submitButton = new JButton("Start");

public trendsPanel() {
    submitButton.addActionListener(this);

  }

  public void actionPerformed(ActionEvent evt) {

	  if (evt.getSource().equals(trendsPanel.submitButton))
	  {
		  Test.findTags(modelFrom.getYear()+"-"+modelFrom.getMonth()+"-"+modelFrom.getDay(), modelTo.getYear()+"-"+modelTo.getMonth()+"-"+modelTo.getDay(), (String)locationList.getSelectedItem());
		  System.exit(0);
		  
	  }
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Astonhack 2015");
    frame.setSize(1250, 200);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    Container contentPane = frame.getContentPane();
    contentPane.setLayout(new FlowLayout());
        
    JPanel descPanel = new JPanel();
    trendsPanel thisPanel = new trendsPanel();
    
    descPanel.setBorder(new TitledBorder("Nishan"));
    
    contentPane.setLayout(new GridLayout(2,1));
    contentPane.add(descPanel,1,0);
    contentPane.add(thisPanel,1,1);
    JLabel labelFrom = new JLabel("From: ");
    JLabel labelTo = new JLabel("To: ");
    JLabel labelWeb = new JLabel("Website(s): ");
    JLabel labelLocation = new JLabel("Location: ");
    
    Map<String,Integer> countryList = Country.loadCountries();
    modelFrom = new UtilDateModel();
    modelTo = new UtilDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    JDatePanelImpl datePanelFrom = new JDatePanelImpl(modelFrom, p);
    JDatePickerImpl datePickerFrom = new JDatePickerImpl(datePanelFrom,new DateLabelFormatter());
    modelFrom.setDate(2015, 11, 06);
    modelTo.setDate(2015, 11, 07);
    modelFrom.setSelected(true);
    modelTo.setSelected(true);
    JDatePanelImpl datePanelTo = new JDatePanelImpl(modelTo, p);
    JDatePickerImpl datePickerTo = new JDatePickerImpl(datePanelTo,new DateLabelFormatter());
    Vector locations = new Vector();
    Object[] keys = countryList.keySet().toArray();
    Arrays.sort(keys);
    for (int i=0;i<keys.length;i++) locations.add(keys[i]);
    locationList = new JComboBox(locations);
    locationList.setSelectedItem("Worldwide");
    JTextArea ta = new JTextArea("", 3, 20);
    ta.setLineWrap(true);
   
    
    thisPanel.add(labelWeb);
    thisPanel.add(ta);
    thisPanel.add(labelLocation);
    thisPanel.add(labelFrom);
    thisPanel.add(locationList);
    thisPanel.add(labelFrom);
    thisPanel.add(datePickerFrom);
    thisPanel.add(labelTo);
    thisPanel.add(datePickerTo);
    thisPanel.add(submitButton);
    frame.setVisible(true);
    }
    
}

class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
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