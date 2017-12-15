package de.uni_potsdam.hpi.asg.asynctoolswrapper;

/*
 * Copyright (C) 2017 Norman Kluge
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

import de.uni_potsdam.hpi.asg.common.invoker.ExternalToolsInvoker;
import de.uni_potsdam.hpi.asg.common.invoker.InvokeReturn;

public class MpsatInvoker extends ExternalToolsInvoker {

    private MpsatInvoker() {
        super("mpsat");
    }

    public static InvokeReturn solveCSC(File inFile, File outFile) {
        return new MpsatInvoker().internalSolveCSC(inFile, outFile);
    }

    private InvokeReturn internalSolveCSC(File inFile, File outFile) {
        //@formatter:off
        List<String> params = Arrays.asList(
            "-R", "-f", "-@", "-p0",
            "-cl", inFile.getName()
        );
        //@formatter:on

        addInputFilesToCopy(inFile);
        addOutputFileToExport("mpsat.g", outFile);

        InvokeReturn ret = run(params, "mpsat");
        errorHandling(ret); //TODO: check okCodes
        return ret;
    }
}
