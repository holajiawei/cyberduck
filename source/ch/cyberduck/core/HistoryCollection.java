package ch.cyberduck.core;

/*
 *  Copyright (c) 2007 David Kocher. All rights reserved.
 *  http://cyberduck.ch/
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *  dkocher@cyberduck.ch
 */

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;

/**
 * @version $Id$
 */
public class HistoryCollection extends AbstractFolderHostCollection {
    private static Logger log = Logger.getLogger(HistoryCollection.class);

    private static HistoryCollection HISTORY_COLLECTION = new HistoryCollection(
            LocalFactory.createLocal(Preferences.instance().getProperty("application.support.path"), "History")
    );

    public HistoryCollection(Local f) {
        super(f);
    }

    /**
     * @return
     */
    public static HistoryCollection defaultCollection() {
        return HISTORY_COLLECTION;
    }

    @Override
    public Local getFile(Host bookmark) {
        return LocalFactory.createLocal(folder, bookmark.getNickname() + ".duck");
    }

    /**
     * Does not allow duplicate entries.
     *
     * @param row
     * @param bookmark
     */
    @Override
    public void add(int row, Host bookmark) {
        if(this.contains(bookmark)) {
            this.remove(bookmark);
        }
        super.add(row, bookmark);
    }

    /**
     * Does not allow duplicate entries.
     *
     * @param bookmark
     * @return
     */
    @Override
    public boolean add(Host bookmark) {
        if(this.contains(bookmark)) {
            this.remove(bookmark);
        }
        return super.add(bookmark);
    }

    /**
     * Sort by timestamp of bookmark file.
     */
    @Override
    protected void sort() {
        Collections.sort(this, new Comparator<Host>() {
            public int compare(Host o1, Host o2) {
                Local f1 = getFile(o1);
                Local f2 = getFile(o2);
                if(f1.attributes().getModificationDate() < f2.attributes().getModificationDate()) {
                    return 1;
                }
                if(f1.attributes().getModificationDate() > f2.attributes().getModificationDate()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * Does not allow manual additions
     *
     * @return False
     */
    @Override
    public boolean allowsAdd() {
        return false;
    }

    /**
     * Does not allow editing entries
     *
     * @return False
     */
    @Override
    public boolean allowsEdit() {
        return false;
    }
}