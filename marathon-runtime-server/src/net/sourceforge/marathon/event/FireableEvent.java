/*******************************************************************************
 *  
 *  Copyright (C) 2010 Jalian Systems Private Ltd.
 *  Copyright (C) 2010 Contributors to Marathon OSS Project
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 * 
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 *  Project website: http://www.marathontesting.com
 *  Help: Marathon help forum @ http://groups.google.com/group/marathon-testing
 * 
 *******************************************************************************/
package net.sourceforge.marathon.event;

import java.awt.AWTEvent;
import java.awt.Component;

import javax.swing.SwingUtilities;

import net.sourceforge.marathon.util.EventQueueRunner;

public abstract class FireableEvent {
    private Component component;
    protected EventQueueRunner eventQueueRunner;

    public FireableEvent(Component component) {
        this.component = component;
        eventQueueRunner = new EventQueueRunner();
    }

    protected void postEvent(final AWTEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getComponent().dispatchEvent(evt);
            }
        });
    }

    protected Component getComponent() {
        return component;
    }
}
