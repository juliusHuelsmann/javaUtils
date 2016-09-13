package utils;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import opaqueFrame.OpFrame;
import spotlight.delphi.utils.Utils;


/**
 * Shows BufferedImage for testing purpose.
 * @author Julius Huelsmann
 *
 */
@SuppressWarnings("serial")
public final class BufferedViewer extends OpFrame {

    
    
    /**
     * The MLabel for the painting.
     */
    private JLabel jlbl_painting;
    
    
    /**
     * The only instance of this class.
     */
    private static BufferedViewer instance;
    
    
    
    /**
     * BufferedViewer constructor.
     * 
     */
    public BufferedViewer() {
        super();
        final int size = 100;
        super.setUndecorated(true);
        super.setSize(size, size);
        
        jlbl_painting = new JLabel();
        jlbl_painting.setSize(getSize());
        super.add(jlbl_painting);
        
        
        super.setVisible(true);
    }

    /**
     * @return the instance
     */
    public static BufferedViewer getInstance() {
        
        if (instance == null) {
            instance = new BufferedViewer();
        }
        return instance;
    }

    
    /**
     * The last time.
     */
    private static double timeLast = 0;
    
    /**
     * Show new bufferedImage.
     * @param _bi the bufferedImage.
     */
    public synchronized void show(final BufferedImage _bi) {
    	
    	final boolean showEnabled = !false;
    	if (showEnabled) {
        	
    		final int minTimeDiff = 5;
    		final int maxWindowSize = 700;
    		

        	final double time = System.currentTimeMillis();

        	if (time - timeLast > minTimeDiff) {
        		timeLast = time;
            	if (_bi.getWidth() > 0
            			&& _bi.getHeight() > 0) {

            		int width = _bi.getWidth();
            		int height = _bi.getHeight();
            		
            		if (_bi.getWidth() > maxWindowSize) {
            			width = maxWindowSize;
            		}
            		if (_bi.getHeight() > maxWindowSize) {
            			height = maxWindowSize;
            		}
            		
            		BufferedImage bi = Utils.resize(_bi, width, height);
            		
                    setSize(bi.getWidth(), bi.getHeight());
                    jlbl_painting.setSize(bi.getWidth(),
                    		bi.getHeight());
                    jlbl_painting.setIcon(new ImageIcon(bi));
                    System.out.println("passed");
            	}
        	}
    	}
    }
}
