package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.rom.myfreetv.view.VText.CompositeIcon;

import org.rom.myfreetv.view.VText.VTextIcon;

import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;

class ChannelPanel extends JPanel implements ActionListener {

   private MyFreeTV owner;
   private ChannelList channelsList;
   private ChannelList channelsListFavoris;
   private ChannelList channelsListRadio;

   private JButton play, rec, prog;

   private JPopupMenu popup = new JPopupMenu();
   private JMenuItem addFavo = new JMenuItem("Ajouter aux favoris");
   private JMenuItem removeFavo = new JMenuItem("Retirer des favoris");
   private JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.LEFT);


   public ChannelPanel(final MyFreeTV owner) {
      super();
      this.owner = owner;
      setLayout(new BorderLayout());
      setBorder(new TitledBorder(null, "Sélection: "));
      initTab();

      addFavo.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            int selectedIndex = channelsList.getSelectedIndex();
            if(selectedIndex >= 0) {
               Channel channel = (Channel) channelsList.getModel().getElementAt(selectedIndex);
               ChannelManager.getInstance().addFavoris(channel);
            }
         }
      });

      removeFavo.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            int selectedIndex = channelsList.getSelectedIndex();
            if(selectedIndex >= 0) {
               Channel channel = (Channel) channelsList.getModel().getElementAt(selectedIndex);
               ChannelManager.getInstance().removeFavoris(channel);
               channelsList.refresh();
            }
         }
      });

      channelsList.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            initPopup(e);
         }
      });
      channelsListRadio.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            initPopup(e);
         }
      });
      channelsListFavoris.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            initPopup(e);
         }
      });
      channelsList.addListSelectionListener(owner);
      channelsListRadio.addListSelectionListener(owner);
      channelsListFavoris.addListSelectionListener(owner);

      add(tabPanel, BorderLayout.CENTER);

      JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      play = new JButton(ImageManager.getInstance().getImageIcon("play"));
      rec = new JButton(ImageManager.getInstance().getImageIcon("record"));
      prog = new JButton(ImageManager.getInstance().getImageIcon("prog"));
//        play.setBorder(MyFreeTV.buttonBorder);
//        rec.setBorder(MyFreeTV.buttonBorder);
//        prog.setBorder(MyFreeTV.buttonBorder);
      play.setToolTipText("Regarder la chaîne sélectionnée.");
      rec.setToolTipText("Enregistrer la chaîne sélectionnée dans un fichier.");
      prog.setToolTipText("Programmer un enregistrement.");
      play.setActionCommand("play");
      rec.setActionCommand("rec");
      prog.setActionCommand("prog");
      play.addActionListener(this);
      rec.addActionListener(this);
      prog.addActionListener(this);
      bottomPanel.add(play);
      bottomPanel.add(rec);
      bottomPanel.add(prog);
      add(bottomPanel, BorderLayout.SOUTH);
   }
   private void initPopup(MouseEvent e) {

      popup.removeAll();
      int type = tabPanel.getSelectedIndex();
      if(e.getButton() == MouseEvent.BUTTON3) {
         int selectedIndex = channelsList.locationToIndex(e.getPoint());
         channelsList.setSelectedIndex(selectedIndex);
         if(selectedIndex >= 0) {
            Channel channel = (Channel) channelsList.getModel().getElementAt(selectedIndex);
            if(type == 0 ) {
               addFavo.setText("Ajouter "+channel.getName()+" aux favoris");
               popup.add(addFavo);
            }
            if(type == 1) {
               addFavo.setText("Ajouter "+channel.getName()+" aux favoris");
               popup.add(addFavo);
            }
            if(type == 2) {
               removeFavo.setText("Retirer "+channel.getName()+" des favoris");
               popup.add(removeFavo);
            }
            popup.show(e.getComponent(), e.getX(), e.getY());
         }
      }
      if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
         int selectedIndex = channelsList.locationToIndex(e.getPoint());
         if(selectedIndex >= 0) {
            Channel channel = (Channel) channelsList.getModel().getElementAt(selectedIndex);
            if(!JobManager.getInstance().isPlaying(channel))
               owner.getActions().play(channel);
         }
      }
   }
   private void initTab() {
      Icon graphicIcon = ImageManager.getInstance().getImageIcon("TV");
      CompositeIcon iconTV = new CompositeIcon(graphicIcon,
        new VTextIcon(tabPanel, "FreeTV", VTextIcon.ROTATE_DEFAULT));
      graphicIcon = ImageManager.getInstance().getImageIcon("radio");
      CompositeIcon iconRadio = new CompositeIcon(graphicIcon,
        new VTextIcon(tabPanel, "Radios", VTextIcon.ROTATE_DEFAULT));
      graphicIcon = ImageManager.getInstance().getImageIcon("favoris");
      CompositeIcon iconFavoris = new CompositeIcon(graphicIcon,
        new VTextIcon(tabPanel, "Favoris", VTextIcon.ROTATE_DEFAULT));

      channelsList = new ChannelList();
      channelsListRadio =new ChannelList();
      channelsListFavoris = new ChannelList();

      JScrollPane scrollTV = new JScrollPane();
      JScrollPane scrollRadio = new JScrollPane();
      JScrollPane scrollFavoris = new JScrollPane();

      tabPanel.addTab(null, iconTV, scrollTV);
      tabPanel.addTab(null, iconRadio, scrollRadio);
      tabPanel.addTab(null, iconFavoris, scrollFavoris);

      scrollTV.setViewportView(channelsList);
      scrollRadio.setViewportView(channelsListRadio);
      scrollFavoris.setViewportView(channelsListFavoris);

      tabPanel.addChangeListener(new ChangeListener() {
         // This method is called whenever the selected tab changes
         public void stateChanged(ChangeEvent evt) {
            switch(tabPanel.getSelectedIndex()) {
               case 0:
                  ChannelManager.getInstance().changeChannel(ChannelManager.ChannelType.TV);
                  break;
               case 1:
                  ChannelManager.getInstance().changeChannel(ChannelManager.ChannelType.RADIO);
                  break;
               case 2:
                  ChannelManager.getInstance().changeChannel(ChannelManager.ChannelType.FAVORIS);
                  break;
               default:
                  ChannelManager.getInstance().changeChannel(ChannelManager.ChannelType.TV);
            }
            channelsList.refresh();
         }
      });

      tabPanel.setPreferredSize(new Dimension(200, 350));
   }

   public ChannelList getChannelsList() {
      switch(tabPanel.getSelectedIndex()) {
         case 0:
            return channelsList;
         case 1:
            return channelsListRadio;
         case 2:
            return channelsListFavoris;
         default:
            return channelsList;
      }
   }

   public void initButtons() {
      ChannelList cList = getChannelsList();
      int selectedIndex = cList.getSelectedIndex();
      if(selectedIndex < 0) {
         play.setEnabled(false);
         rec.setEnabled(false);
         prog.setEnabled(false);
      } else {
         Channel channel = (Channel) cList.getModel().getElementAt(selectedIndex);
         play.setEnabled(!JobManager.getInstance().isPlaying(channel));
         rec.setEnabled(!JobManager.getInstance().isRecording(channel));
         prog.setEnabled(true);
      }
   }

   public void actionPerformed(ActionEvent e) {
      ChannelList cList = getChannelsList();

      int selectedIndex = cList.getSelectedIndex();
      if(selectedIndex >= 0) {
         String s = e.getActionCommand();
         Channel channel = (Channel) cList.getModel().getElementAt(selectedIndex);
//          ChannelManager.getInstance().getChannels().get(selectedIndex);
         if(s.equals("play")) {
            owner.getActions().play(channel);
         } else if(s.equals("rec")) {
            owner.getActions().record(channel);
         } else if(s.equals("prog")) {
            owner.getActions().prog(channel);
         }
      }
   }

}
