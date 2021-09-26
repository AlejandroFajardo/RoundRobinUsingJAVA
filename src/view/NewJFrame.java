/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import models.Process;
import models.SO;

/**
 *
 * @author jhona
 */
public class NewJFrame extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form NewJFrame
     */
    Thread hilo;
    int option = 0;
    ArrayList<Process> listProcess;
    ArrayList<Process> listExecute = new ArrayList<Process>();

    public SO sistemO = new SO(0);

    public void createSO(int timeQuantum) {
        sistemO = new SO(timeQuantum);
    }

    public void addProcess(Process proc) {
        sistemO.addProcess(proc);
    }

    public void startProcess() throws InterruptedException {
        ArrayList<Process> queue = new ArrayList<>();
        boolean aux = true;
        int timer = 0;
        int auxQuantum = sistemO.quantum;
        int auxPosition = 0;
        int quantum = 0;
        LinkedList<Process> listQueue = new LinkedList<Process>();
        sistemO.list = listExecute;

        try {

            do {

                timer++;
                quantum++;
                for (Process process : sistemO.list) {
                    if (process.timeArrival == timer) {
                        queue.add(process);
                        listQueue.addLast(process);
                        generarProcesoUser2(new ArrayList<Process>(listQueue));
                    }
                }
                listQueue.get(auxPosition).timeProcess = listQueue.get(auxPosition).timeProcess - 1;
                listQueue.get(auxPosition).state = 'P';

                generarProcesoUser1(this.listExecute);
                generarProcesoUser2(new ArrayList<Process>(listQueue));
                Thread.sleep(jSliderVelocidad.getValue() * 2 * 10);
                JButton procces = new JButton();
                procces.setBackground(new Color(Integer.parseInt(listQueue.get(auxPosition).color.split(",")[0]), Integer.parseInt(listQueue.get(auxPosition).color.split(",")[1]), Integer.parseInt(listQueue.get(auxPosition).color.split(",")[2])));
                procces.setMinimumSize(new Dimension(20, 20));
                procces.setMaximumSize(new Dimension(20, 20));
                jPanelCpuProcess.add(procces);

                generarProcesoUser1(this.listExecute);
                generarProcesoUser2(new ArrayList<Process>(listQueue));

                Thread.sleep(jSliderVelocidad.getValue() * 2 * 10);

                System.out.println(quantum + " -- " + auxQuantum);
                System.out.println(queue.get(auxPosition).id + " -- " + queue.get(auxPosition).timeProcess);

                if (listQueue.get(auxPosition).timeProcess <= 0) {

                    listQueue.get(auxPosition).state = 'F';
                    generarProcesoUser1(this.listExecute);
                    listQueue.remove(listQueue.get(auxPosition));
                    generarProcesoUser2(new ArrayList<Process>(listQueue));

                    System.out.println("termino proceso");

                    auxPosition = 0;
                    if (auxPosition >= listQueue.size()) {
                        auxPosition = 0;
                    }
                }

                if (quantum == auxQuantum) {
                    System.out.println("se cumplio el quantum");

                    Process auxProc = listQueue.get(auxPosition);
                    if (auxProc.priority > 1) {
                        auxProc.priority--;
                    }
                    auxProc.state = 'W';
                    listQueue.get(auxPosition).state = 'W';
                    listQueue.remove(listQueue.get(auxPosition));
                    listQueue.addLast(auxProc);
                    generarProcesoUser1(this.listExecute);
                    generarProcesoUser2(new ArrayList<Process>(listQueue));

                    int mayor = 0;
                    for (int i = 0; i < listQueue.size(); i++) {

                        if (listQueue.get(i).priority > mayor) {
                            mayor = listQueue.get(i).priority;
                        }
                    }
                    for (int i = 0; i < listQueue.size(); i++) {
                        if (listQueue.get(i).priority == mayor) {
                            Process auxP = listQueue.get(i);
                            listQueue.remove(auxP);
                            listQueue.addFirst(auxP);
                            break;
                        }
                    }

                    auxPosition = 0;
                    quantum = 0;

                }
                System.out.println(":" + listQueue.size());
                if (listQueue.size() <= 0) {
                    jPanelQueue2.removeAll();
                    System.out.println("Muerete");
                    revalidate();
                    repaint();
                    hilo.stop();
                    aux = false;
                    break;
                }
            } while (aux);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public NewJFrame() {

        initComponents();
        setTitle("Simulador de Operaciones de un Sistema Operativo");
        this.getContentPane().setBackground(Color.WHITE);
        jPanelQueue.setLayout(new GridLayout(10, 1));
        jPanelQueue2.setLayout(new GridLayout(10, 1));
        jPanelCpuProcess.setLayout(new BoxLayout(jPanelCpuProcess, BoxLayout.LINE_AXIS));
        jPanel1.setBackground(Color.WHITE);
        jPanel2.setBackground(Color.WHITE);
        jPanel3.setBackground(Color.WHITE);
        jPanelCpuProcess.setBackground(Color.WHITE);
        jPanelQueue.setBackground(Color.WHITE);
        jPanelQueue2.setBackground(Color.WHITE);

    }

    public void generarProcesoUser1(ArrayList<Process> listProc) {

        jPanelQueue.removeAll();
        for (Iterator<Process> iterator = listProc.iterator(); iterator.hasNext();) {
            Process next = iterator.next();
            JButton procces = new JButton("" + next.state + "    \n" + next.priority);
            procces.setForeground(Color.WHITE);
            procces.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            procces.setBackground(new Color(Integer.parseInt(next.color.split(",")[0]), Integer.parseInt(next.color.split(",")[1]), Integer.parseInt(next.color.split(",")[2])));
            jPanelQueue.add(procces);
            revalidate();
            repaint();
        }

    }

    public void generarProcesoUser2(ArrayList<Process> listProc) {

        jPanelQueue2.removeAll();
        for (Iterator<Process> iterator = listProc.iterator(); iterator.hasNext();) {
            Process next = iterator.next();
            JButton procces = new JButton("" + next.state + "    \n" + next.priority);
            procces.setForeground(Color.WHITE);
            procces.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            procces.setBackground(new Color(Integer.parseInt(next.color.split(",")[0]), Integer.parseInt(next.color.split(",")[1]), Integer.parseInt(next.color.split(",")[2])));
            jPanelQueue2.add(procces);
            revalidate();
            repaint();
        }

    }

    public void paintSerie(ArrayList<Process> listProc) throws InterruptedException {

        for (Iterator<Process> iterator = listProc.iterator(); iterator.hasNext();) {
            Process next = iterator.next();

            startProcess();
            //next.state = 'F';
            generarProcesoUser1(this.listExecute);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelQueue = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jSliderVelocidad = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabelTiempo = new javax.swing.JLabel();
        jPanelQueue2 = new javax.swing.JPanel();
        addProcess = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonPlay = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanelCpuProcess = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanelQueue.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cola de procesos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        jPanelQueue.setMaximumSize(new java.awt.Dimension(154, 600));
        jPanelQueue.setMinimumSize(new java.awt.Dimension(154, 600));
        jPanelQueue.setPreferredSize(new java.awt.Dimension(154, 600));

        javax.swing.GroupLayout jPanelQueueLayout = new javax.swing.GroupLayout(jPanelQueue);
        jPanelQueue.setLayout(jPanelQueueLayout);
        jPanelQueueLayout.setHorizontalGroup(
            jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelQueueLayout.setVerticalGroup(
            jPanelQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parametros", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jSliderVelocidad.setBackground(new java.awt.Color(255, 255, 255));
        jSliderVelocidad.setForeground(new java.awt.Color(255, 255, 255));
        jSliderVelocidad.setMaximum(50);
        jSliderVelocidad.setValue(1);
        jPanel4.add(jSliderVelocidad);

        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jButton3.setText("Limpiar");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel5.add(jButton3);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tiempo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabelTiempo.setBackground(new java.awt.Color(255, 255, 255));
        jLabelTiempo.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 308, 44, 310);
        jPanel6.add(jLabelTiempo, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelQueue2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cola de Ejecucion ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        jPanelQueue2.setMaximumSize(new java.awt.Dimension(154, 600));
        jPanelQueue2.setMinimumSize(new java.awt.Dimension(154, 600));
        jPanelQueue2.setName(""); // NOI18N
        jPanelQueue2.setPreferredSize(new java.awt.Dimension(154, 600));

        javax.swing.GroupLayout jPanelQueue2Layout = new javax.swing.GroupLayout(jPanelQueue2);
        jPanelQueue2.setLayout(jPanelQueue2Layout);
        jPanelQueue2Layout.setHorizontalGroup(
            jPanelQueue2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );
        jPanelQueue2Layout.setVerticalGroup(
            jPanelQueue2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        addProcess.setText("Add");
        addProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProcessMouseClicked(evt);
            }
        });
        addProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProcessActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jButtonPlay.setText("Iniciar Proceso");
        jButtonPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonPlayMouseClicked(evt);
            }
        });
        jButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPlay);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("CPU"));

        javax.swing.GroupLayout jPanelCpuProcessLayout = new javax.swing.GroupLayout(jPanelCpuProcess);
        jPanelCpuProcess.setLayout(jPanelCpuProcessLayout);
        jPanelCpuProcessLayout.setHorizontalGroup(
            jPanelCpuProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelCpuProcessLayout.setVerticalGroup(
            jPanelCpuProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCpuProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jPanelCpuProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanelQueue2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addProcess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelQueue2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelQueue2.getAccessibleContext().setAccessibleName("Cola de Ejecucion");
        jPanelQueue2.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addProcessActionPerformed

    private void addProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProcessMouseClicked
        listExecute.add(new Process(Integer.parseInt(JOptionPane.showInputDialog("Tiempo de proceso")), Integer.parseInt(JOptionPane.showInputDialog("Tiempo de llegada")), Integer.parseInt(JOptionPane.showInputDialog("Prioridad"))));
        generarProcesoUser1(listExecute);
    }//GEN-LAST:event_addProcessMouseClicked

    private void jButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPlayActionPerformed

    private void jButtonPlayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPlayMouseClicked

        option = 1;
        sistemO.quantum = Integer.parseInt(JOptionPane.showInputDialog("Quantum"));
        hilo = new Thread(this);
        hilo.start();


    }//GEN-LAST:event_jButtonPlayMouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        jPanelCpuProcess.removeAll();
        jPanelQueue.removeAll();
        jPanelQueue2.removeAll();
        sistemO.list.clear();
        //user1 = new User("User 1");
        // user2 = new User("User 1");
        repaint();
        revalidate();
    }//GEN-LAST:event_jButton3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addProcess;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonPlay;
    private javax.swing.JLabel jLabelTiempo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelCpuProcess;
    private javax.swing.JPanel jPanelQueue;
    private javax.swing.JPanel jPanelQueue2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSliderVelocidad;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {

        try {
            if (option == 1) {
                paintSerie(listExecute);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
