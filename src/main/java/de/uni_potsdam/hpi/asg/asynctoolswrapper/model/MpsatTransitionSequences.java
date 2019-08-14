package de.uni_potsdam.hpi.asg.asynctoolswrapper.model;

/*
 * Copyright (C) 2019 Norman Kluge
 * 
 * This file is part of ASGwrapper-asynctools.
 * 
 * ASGwrapper-asynctools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASGwrapper-asynctools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASGwrapper-asynctools.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.asg.common.iohelper.FileHelper;

public class MpsatTransitionSequences {

    private boolean           pathFound;
    private Set<List<String>> sequences;

    private MpsatTransitionSequences(boolean pathFound, Set<List<String>> sequences) {
        this.pathFound = pathFound;
        this.sequences = sequences;
    }

    public static MpsatTransitionSequences createFromOutFile(File outFile) {
        if(!outFile.exists()) {
            return new MpsatTransitionSequences(false, null);
        }

        boolean found = false;
        Set<List<String>> sequences = new HashSet<>();

        List<String> lines = FileHelper.getInstance().readFile(outFile);
        for(String line : lines) {
            if(line.startsWith("YES")) {
                found = true;
            } else if(line.startsWith("_SEQUENCE:")) {

            } else if(line.equals("")) {

            } else {
                List<String> seq = new ArrayList<>();
                String[] split = line.split(",");
                for(String entry : split) {
                    seq.add(entry.trim());
                }
                sequences.add(seq);
            }
        }
        return new MpsatTransitionSequences(found, sequences);
    }

    public boolean isPathFound() {
        return pathFound;
    }

    public Set<List<String>> getSequences() {
        return sequences;
    }
}
