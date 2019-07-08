package filearchives;
/**
 * MÓJ PROJEKT PROGRAMU DO ARCHIWIZOWANIA DANYCH
 * @author Rafał Korzeniec
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.*;
public class FileArchives extends JFrame 
{     
    private final JList list;
    private JButton bAdd;
    private JButton bDelete;
    private JButton bZip;
    private JButton info;
    private JMenuBar menuBelt = new JMenuBar();
    private JFileChooser chooser = new JFileChooser();
    private JLabel time = new JLabel("Time: ");
    private JLabel chronos = new JLabel(getTime());
    private JFrame aboutFrame = this;
    
public FileArchives()
{
    this.list = new JList(listModel);
    this.setTitle("File Archives");
    this.setBounds(200, 200, 500, 500);
    this.setJMenuBar(menuBelt);
    JMenu menuFile = menuBelt.add(new JMenu("File"));
    JMenu menuFile2 = menuBelt.add(new JMenu("About project"));
  
Action actionAdd = new Action("Add", "Add to archives", "ctrl A");
Action actionDelete = new Action("Delete","Delete files", "ctrl D");
Action actionZip = new Action("Packing", "Compression files", "ctrl Z");
   
    JMenuItem menuOpen = menuFile.add(actionAdd);
    JMenuItem menuDelete = menuFile.add(actionDelete);
    JMenuItem menuZip = menuFile.add(actionZip);
    menuFile2.add(new JMenuItem("Data archiving software. Author: Rafał Korzeniec"));
    bAdd = new JButton(actionAdd);
    bDelete = new JButton(actionDelete);
    bZip = new JButton(actionZip);
    info = new JButton("Info");
    JScrollPane scroll = new JScrollPane(list);
    
    info.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            new Infor(aboutFrame).setVisible(true);
        }
    });
    
    ActionListener stoper = new clock();
    Timer clock = new Timer(1000, stoper);
    clock.start();
        
    list.setBorder(BorderFactory.createEtchedBorder()); 
    GroupLayout layout = new GroupLayout(this.getContentPane()); 
    
    layout.setHorizontalGroup(layout.createParallelGroup().addComponent
        (scroll, GroupLayout.DEFAULT_SIZE, 
        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(time).addComponent(chronos)
        .addGroup(layout.createSequentialGroup()
         .addComponent(bAdd).addComponent(bDelete).addComponent(info)
           .addGap(5, 40, Short.MAX_VALUE).addComponent(bZip))
            );
    layout.setAutoCreateContainerGaps(true);
    layout.setAutoCreateGaps(true);
    layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(time).addComponent(chronos).addComponent
        (scroll, 100, 150, Short.MAX_VALUE).addContainerGap
        (0, Short.MAX_VALUE).addGroup(layout.createParallelGroup()
       .addComponent(bAdd).addComponent(bDelete).addComponent(info)
                .addComponent(bZip)));
    
    this.getContentPane().setLayout(layout);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
}
private DefaultListModel listModel = new DefaultListModel()
{
    @Override
        public void addElement(Object obj) 
        {
            list.add(obj);
            super.addElement(((File)obj).getName());
        }
        @Override
        public Object get(int index)
        {
            return list.get(index);
        }
        @Override
        public Object remove(int index)
        {
            list.remove(index);
            return super.remove(index);
        }        
        ArrayList list = new ArrayList();
};
    
    public static void main(String[] args) 
    {
        new FileArchives().setVisible(true);
    }
    
private class Action extends AbstractAction
    {
        public Action(String name, String about, String key)
        {
            this.putValue(Action.NAME, name);
            this.putValue(Action.SHORT_DESCRIPTION, about);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key));
        }
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getActionCommand().equals("Add"))
                addFileToArchives();
            else if(e.getActionCommand().equals("Delete"))
                removeFileFromArchives();
            else if(e.getActionCommand().equals("Packing"))
                createArchives();        
        }
        public void addFileToArchives()
        {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(enabled);
            int tmp = chooser.showDialog(rootPane, "Add to archives");
            
            if(tmp == JFileChooser.APPROVE_OPTION)
            {
                File[] stream = chooser.getSelectedFiles();
                
                for(int i = 0; i < stream.length; i++)//aby plik sie nie powtarzał
                    if(!areRepeat(stream[i].getPath()))
                        listModel.addElement(stream[i]);
            }
        }
/*
Powyżej ustawienie co chcemy i jak dodawać/więcej niż jeden plik.
        Poniżej metoda która obsługuję pętlę z warunkiem u góry - 
        -ma za zadanie "filtrowanie" plików aby się nie powtarzały
*/
        private boolean areRepeat(String test)
        {
            for(int i = 0; i < listModel.getSize(); i++)
                if(((File)listModel.get(i)).getPath().equals(test))
                return true;
                
            return false;
        }
        /*
        Poniżej metoda usuwania plików
        */
        public void removeFileFromArchives()
        {
            int[] tmp = list.getSelectedIndices();
              for(int i = 0; i < tmp.length; i++)
                  listModel.remove(tmp[i]-i);
        }        
        public void createArchives()
        {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            chooser.setSelectedFile(new File(System.getProperty("user.dir")+File.separator+"xyz.zip"));
            int tmp = chooser.showDialog(rootPane, "Packing");
            
            if(tmp == JFileChooser.APPROVE_OPTION)
            {
               byte tmpData[] = new byte[BUFFOR];
               try
               {
                   ZipOutputStream ZOS = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(chooser.getSelectedFile()), BUFFOR));
                   
                   for(int i = 0; i < listModel.getSize(); i++)
                   {
                       if(!((File)listModel.get(i)).isDirectory())
                            zipp(ZOS, (File)listModel.get(i), tmpData, 
                              ((File)listModel.get(i)).getPath());
                       else
                       {
                            writeStream((File)listModel.get(i));

                            for(int y = 0; y < streamList.size(); y++)
                            zipp(ZOS, (File)streamList.get(y), tmpData, 
                                  ((File)listModel.get(y)).getPath());

                            streamList.removeAll(streamList);
                        }
                   }
                   ZOS.close();
               }
               catch(IOException e)
               {
                   System.out.println(e.getMessage());
               }
            }
        }
        
        private void zipp(ZipOutputStream ZOS, File streamFile, 
        byte[]tmpData, String baseStream) throws IOException
        {
            BufferedInputStream InS = new BufferedInputStream(new FileInputStream(streamFile), BUFFOR);
            ZOS.putNextEntry(new ZipEntry(streamFile.getPath().substring(baseStream.lastIndexOf(File.separator)+1)));
            int counter;
            while((counter = InS.read(tmpData, 0, BUFFOR)) != -1)
                   ZOS.write(tmpData, 0, counter);
            
            ZOS.closeEntry();
            InS.close();
        }
        public static final int BUFFOR = 1024;
        
        private void writeStream(File streamName)
        {
            String[] namesOfFilesAndCatalogues = streamName.list();
            System.out.println(streamName.getPath());
            
            for(int i = 0; i < namesOfFilesAndCatalogues.length; i++)
            {
                File files = new File(streamName.getPath(), namesOfFilesAndCatalogues[i]);

                System.out.println(files.getPath());//wypis sciezek wszystkich plików

                if (files.isFile()) // aby znalezc tylko pliki
                      streamList.add(files);

                if (files.isDirectory()) writeStream(new File(files.getPath()));               
            }
        }
        ArrayList streamList = new ArrayList();
    }

private class clock implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            chronos.setText(getTime());
        }        
    }
public String getTime()
    {
       GregorianCalendar timeApp = new GregorianCalendar();
//       String d = ""+timeApp.get(Calendar.DAY_OF_MONTH);
//       String mth = ""+timeApp.get(Calendar.MONTH);
//       String y = ""+timeApp.get(Calendar.YEAR);
       String h = ""+timeApp.get(Calendar.HOUR); 
       String m = ""+timeApp.get(Calendar.MINUTE);
       String s = ""+timeApp.get(Calendar.SECOND);

//       if(Integer.parseInt(mth) < 10)
//           mth = "0" + mth;
       if(Integer.parseInt(h) < 10)
           h = "0" + h;
       if(Integer.parseInt(m) < 10)
           m = "0" + m;
       if(Integer.parseInt(s) < 10)
           s = "0" + s;
return h + " : " + m + " : " + s;
    }
class Infor extends JDialog 
    {
        public Infor(JFrame parent)
        {
            super (parent, true);//po włączeniu apki nie da się przejść niżej
            initComponents();

            int width = (int)parent.getBounds().getWidth();
            int height = (int)parent.getBounds().getHeight();

            int widthFrame = this.getSize().width;
            int heightFrame = this.getSize().height;

            this.setLocation(parent.getBounds().x+(width-widthFrame)/2, 
                    parent.getBounds().y+(height-heightFrame/2));
        }
        public void initComponents()
        {
            this.setTitle("Mój projekt");
            this.setBounds(200, 200, 500, 200);
            
            this.add(label, BorderLayout.SOUTH);
            
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }       
    private JLabel label = new JLabel("Data archiving software. Author: Rafał Korzeniec");       
    }
}

