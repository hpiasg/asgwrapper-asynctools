package de.uni_potsdam.hpi.asg.asynctoolswrapper;

/*
 * Copyright (C) 2018 Norman Kluge
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.asg.common.invoker.ExternalToolsInvoker;
import de.uni_potsdam.hpi.asg.common.invoker.InvokeReturn;
import de.uni_potsdam.hpi.asg.common.iohelper.FileHelper;
import de.uni_potsdam.hpi.asg.common.iohelper.WorkingdirGenerator;

public class PcompInvoker extends ExternalToolsInvoker {

    private PcompInvoker() {
        super("pcomp");
    }

    public static InvokeReturn parallelComposeSTGs(Set<File> inFiles, File outFile) {
        return new PcompInvoker().internalParallelComposeSTGs(inFiles, outFile);
    }

    private InvokeReturn internalParallelComposeSTGs(Set<File> inFiles, File outFile) {
        StringBuilder str = new StringBuilder();
        for(File f : inFiles) {
            str.append(f.getName() + System.getProperty("line.separator"));
        }
        String str2 = str.substring(0, str.length() - 1);
        File fileList = new File(WorkingdirGenerator.getInstance().getWorkingDir(), "pcomplist.txt");
        FileHelper.getInstance().writeFile(fileList, str2);

        //@formatter:off
        List<String> params = Arrays.asList(
            "-p", // remove unneeded places
            "-i", // common interface -> internal signals
//            "-d", // common interface -> dummy
            "@" + fileList.getName()
        );
        //@formatter:on

        for(File f : inFiles) {
            addInputFilesToCopy(f);
        }
        addInputFilesToCopy(fileList);

        InvokeReturn ret = run(params, "pcomp");
        if(errorHandling(ret)) { //TODO: check okCodes
            FileHelper.getInstance().writeFile(outFile, ret.getOutputStr());
        }

        return ret;
    }
}
