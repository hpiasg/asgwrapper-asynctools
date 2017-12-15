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

public class PunfInvoker extends ExternalToolsInvoker {

    private PunfInvoker() {
        super("punf");
    }

    public static InvokeReturn convertGtoMci(File inFile, File outFile) {
        return new PunfInvoker().internalConvertGtoMci(inFile, outFile);
    }

    private InvokeReturn internalConvertGtoMci(File inFile, File outFile) {
        //@formatter:off
        List<String> params = Arrays.asList(
            "-m=", outFile.getName(), 
            "-f=" + inFile.getName()
        );
        //@formatter:on

        addInputFilesToCopy(inFile);
        addOutputFilesToExport(outFile);

        InvokeReturn ret = run(params, "punf");
        errorHandling(ret);
        return ret;
    }
}
