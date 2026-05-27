package com.polydes.scenelink.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

import com.polydes.scenelink.SceneLinkExtension;
import com.polydes.scenelink.data.LinkPageModel;
import com.polydes.scenelink.ui.combos.PageComboModel;

import stencyl.app.comp.FlatPaintPanel;
import stencyl.app.comp.ToolbarToggleButton;
import stencyl.app.comp.UI;
import stencyl.app.ext.res.AppResourceLoader;
import stencyl.app.ext.res.AppResources;
import stencyl.app.lnf.Theme;
import stencyl.core.util.OS;

public class MainPage extends JPanel
{
	private static AppResources res = AppResourceLoader.getResources("com.polydes.scenelink");
	
	private static MainPage _instance = null;
	
	private HashMap<Integer, LinkPage> pages = null;
	private JPanel topBar = null;
	private FlatPaintPanel leftBar = null;
	private JPanel absoluteWrapper = null;
	private PropertiesPage properties = null;
	
	private JComboBox<LinkPageModel> pageChooser = null;
	private LinkPageModel currentPageModel = null;
	private LinkPage currentPage = null;
	
	private ButtonGroup toolGroup;
	private Tool selectedTool;
	
	public static MainPage get()
	{
		if(_instance == null)
			_instance = new MainPage();
		
		return _instance;
	}
	
	private MainPage()
	{
		super(new BorderLayout());
		setBackground(Theme.LIGHT_BG_COLOR);
		pages = new HashMap<Integer, LinkPage>();
		createTopBar();
		createLeftBar();
		
		switchToPage(0);
	}
	
	private void createTopBar()
	{
		topBar = UI.createButtonPanel();
		topBar.setBorder
        (
        	BorderFactory.createCompoundBorder
        	(
        		BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR),
        		BorderFactory.createEmptyBorder(5, 5, 5, 5)
			)
        );
        	
		pageChooser = new JComboBox<LinkPageModel>(new PageComboModel());
		Dimension chooserSize = new Dimension(200, pageChooser.getPreferredSize().height);
		pageChooser.setPreferredSize(chooserSize);
		pageChooser.setMaximumSize(chooserSize);
		pageChooser.setMinimumSize(chooserSize);
		pageChooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LinkPageModel switchTo = (LinkPageModel) pageChooser.getSelectedItem();
				if(switchTo != null && switchTo != currentPageModel)
					switchToPage((switchTo).getId());
			}
		});
		
		JButton newPage = new JButton("New Page");
		newPage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				LinkPageModel m = SceneLinkExtension.generateNewModel();
				switchToPage(m.getId());
			}
		});
		
		topBar.add(Box.createHorizontalStrut(10));
		topBar.add(pageChooser);
		topBar.add(Box.createHorizontalStrut(15));
		topBar.add(newPage);
		topBar.add(Box.createHorizontalGlue());
	}
	
	private void createLeftBar()
	{
		toolGroup = new ButtonGroup();
		
		leftBar = new FlatPaintPanel(FlatPaintPanel.BUTTON_BAR, FlatPaintPanel.HORIZONTAL);
		leftBar.setBorder
        (
        	BorderFactory.createCompoundBorder
        	(
        		BorderFactory.createMatteBorder(1, 1, 1, 0, Theme.BG_COLOR),
        		BorderFactory.createEmptyBorder(3, 3, 3, 3)
			)
        );
        
		leftBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 3));
		
		AbstractButton viewButton = createToggleButton();
		viewButton.setIcon(res.loadIcon("buttons/view_link.png"));
		leftBar.add(viewButton);
		toolGroup.add(viewButton);
		
		AbstractButton createButton = createToggleButton();
		createButton.setIcon(res.loadIcon("buttons/create_link.png"));
		leftBar.add(createButton);
		toolGroup.add(createButton);
		
		AbstractButton selectButton = createToggleButton();
		selectButton.setIcon(res.loadIcon("buttons/select_link.png"));
		leftBar.add(selectButton);
		toolGroup.add(selectButton);
		
		viewButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTool(Tool.VIEW);
			}
		});
		
		createButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTool(Tool.CREATE);
			}
		});
		
		selectButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTool(Tool.SELECT);
			}
		});

		leftBar.setSize(33, 1);
		leftBar.setMaximumSize(new Dimension(33, 1));
		leftBar.setPreferredSize(new Dimension(33, 1));
		
		viewButton.setSelected(true);
	}
	
	private JToggleButton createToggleButton()
	{
		final ToolbarToggleButton button = new ToolbarToggleButton();

		button.setText("");
        button.setMargin(new Insets(3, 5, 3, 5));
        button.setIconTextGap(4);
        
        if(OS.isMacOSXForStyling())
		{
        	button.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		}
		
        return button;
	}
	
	private void updateTool(Tool t)
	{
		selectedTool = t;
		
		if(currentPage != null)
		{
			currentPage.setTool(t);
		}
	}
	
	public void switchToPage(int id)
	{
		if(currentPageModel != null && currentPageModel.getId() == id)
			return;
		
		if(!pages.containsKey(id))
		{
			if(!SceneLinkExtension.pages.containsKey(id))
				return;
			pages.put(id, new LinkPage(SceneLinkExtension.pages.get(id)));
		}
		if(properties != null)
		{
			properties.dispose();
			properties = null;
		}
		
		currentPageModel = SceneLinkExtension.pages.get(id);
		currentPage = pages.get(id);
		
		removeAll();
		
		add(topBar, BorderLayout.NORTH);
		pageChooser.setSelectedItem(currentPageModel);
		
		add(leftBar, BorderLayout.WEST);
		updateTool(selectedTool);
		
		absoluteWrapper = new JPanel(null);
		absoluteWrapper.setBorder(BorderFactory.createEmptyBorder());
		currentPage.setAbsoluteWrapper(absoluteWrapper);
		
		JPanel centerWrapper = new JPanel(new GridBagLayout());
		centerWrapper.setBorder(BorderFactory.createEmptyBorder());
		centerWrapper.setBackground(Theme.LIGHT_BG_COLOR);
		centerWrapper.add(absoluteWrapper, new GridBagConstraints());
		JScrollPane scroller = UI.createScrollPane(centerWrapper);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
		
		properties = PropertiesPage.generatePropertiesPage(SceneLinkExtension.pages.get(id));
		JPanel propertiesWrapper = new JPanel(new BorderLayout());
		propertiesWrapper.setBackground(Theme.LIGHT_BG_COLOR);
		propertiesWrapper.add(properties, BorderLayout.NORTH);
		propertiesWrapper.setPreferredSize(new Dimension(340, 1));
		add(UI.createScrollPane(propertiesWrapper), BorderLayout.EAST);
		
		revalidate();
	}
}
