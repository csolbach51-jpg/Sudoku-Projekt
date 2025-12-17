package org.solbach.sudoku;
import java.awt.*;
import java.io.*;
import java.awt.print.*;
import static java.awt.print.PrinterJob.getPrinterJob;
import static java.lang.System.out;
import javax.swing.*;

class PFrame extends JFrame
{
  FileReader fr = null;
  BufferedReader br = null;
  JTextField[][] entry = null;
 

  public PFrame()
  {
    super();


    entry = new JTextField[9][9];
    for (var i = 0 ;i<9;i++)
            {
              for (var k= 0;k<9;k++)
              {
                entry[i][k] = new JTextField(1);
                entry[i][k].setEditable(false);
                entry[i][k].setColumns(1);
                add(entry[i][k]);
              }
            }
            setLayout(new GridLayout(9,9));
            pack();
            setVisible(true);
            setSize(300,300);
    try
    {
     fr = new FileReader("./sudos.txt");
     br = new BufferedReader(fr);
    }
    catch (IOException iox){out.println("Kann Datei nicht oeffnen");}

 }


}

class PrintSudoku implements Printable
{
  public static PFrame pframe = null;

  @Override
  public int print(Graphics gr,PageFormat pf,int index)
  {
    if(index >= 0)
    {
    gr.translate( 200,80);
    pframe.print(gr);

            out.println(index);
        /*    setMinimumSize(new Dimension(400,400));
            setMaximumSize(new Dimension(600,600));*/

            //GraphicsConfiguration conf = ((Graphics2D)gr).getDeviceConfiguration();
            //((Graphics2D)gr).setTransform(conf.getDefaultTransform());
            //((Graphics2D)gr).transform(conf.getNormalizingTransform());
            try
            {
              for (var i = 0;i<9;i++)
              {
                   var values = pframe.br.readLine();
                   for (var k = 0;k<9;k++)
                   {
                       if(values.substring(k+1,k+2).equals("0"))
                        pframe.entry[i][k].setText(" ");
                       else
                        pframe.entry[i][k].setText(values.substring(k+1,k+2));
                   }

              }
              pframe.br.readLine();
            }catch(IOException ex){return NO_SUCH_PAGE;}
    }
       else
       {
          return NO_SUCH_PAGE;
       }

    gr.translate(0,340);
    try
    {
              pframe.print(gr);
              return PAGE_EXISTS;
    }catch(Exception ioex){out.println("seitenfehler");return NO_SUCH_PAGE;}
   }


      public static void main(String args[]) throws PrinterException
      {
            pframe = new PFrame();
            pframe.setMinimumSize(new Dimension(500,500));
            pframe.setMaximumSize(new Dimension(500,500));
            pframe.setPreferredSize(new Dimension(500,500));
            var ps = new PrintSudoku();
            var pj = getPrinterJob();
            pj.setPrintable(ps,pj.pageDialog(new PageFormat()));
            pj.print();
      }
}
