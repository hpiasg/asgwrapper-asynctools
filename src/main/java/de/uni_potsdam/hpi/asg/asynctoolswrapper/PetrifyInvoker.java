package de.uni_potsdam.hpi.asg.asynctoolswrapper;

/*
 * Copyright (C) 2017 - 2019 Norman Kluge
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
import java.util.Arrays;
import java.util.List;

import de.uni_potsdam.hpi.asg.common.invoker.ExternalToolsInvoker;
import de.uni_potsdam.hpi.asg.common.invoker.InvokeReturn;

public class PetrifyInvoker extends ExternalToolsInvoker {

    private PetrifyInvoker() {
        super("petrify");
    }

    public static InvokeReturn solveCSC(File inFile, File logFile, File outFile) {
        return new PetrifyInvoker().internalSolveCSC(inFile, logFile, outFile);
    }

    public static InvokeReturn synthesiseWithTM(File inFile, File outFile, File libFile, File logFile) {
        return new PetrifyInvoker().internalSynthesise(inFile, outFile, libFile, logFile, true);
    }

    public static InvokeReturn synthesiseWithoutTM(File inFile, File outFile, File logFile) {
        return new PetrifyInvoker().internalSynthesise(inFile, outFile, null, logFile, false);
    }

    public static InvokeReturn deriveFunctionsWithTM(File inFile, File libFile, File logFile, File stgOutFile, File eqnOutFile) {
        return new PetrifyInvoker().internalDeriveFunctionsWithTM(inFile, libFile, logFile, stgOutFile, eqnOutFile);
    }

    public static InvokeReturn processSTG(File stgInFile, File logFile, File stgOutFile) {
        return new PetrifyInvoker().internalProcessSTG(stgInFile, logFile, stgOutFile);
    }

    private InvokeReturn internalSolveCSC(File inFile, File logFile, File outFile) {
        //@formatter:off
        List<String> params = Arrays.asList(
            "-csc", "-dead", 
            "-o", outFile.getName(), 
            "-log", logFile.getName(), 
            inFile.getName()
        );
        //@formatter:on

        addInputFilesToCopy(inFile);
        addOutputFilesToExport(outFile, logFile);

        InvokeReturn ret = run(params, "petrifycsc_" + inFile.getName());
        errorHandling(ret);
        return ret;
    }

    private InvokeReturn internalSynthesise(File inFile, File outFile, File libFile, File logFile, boolean tm) {
        List<String> params = new ArrayList<>();
        //@formatter:off
        params.addAll(Arrays.asList(
            "-no", "-rst1", "-dead", 
            "-vl", outFile.getName(),
            "-log", logFile.getName() 
        ));
        //@formatter:on
        if(tm) {
            params.add("-tm");
            params.add("-lib");
            params.add(libFile.getName());
        }
        params.add(inFile.getName());

        addInputFilesToCopy(inFile);
        if(tm) {
            addInputFilesToCopy(libFile);
        }
        addOutputFilesToExport(outFile, logFile);

        InvokeReturn ret = run(params, "petrifysyn_" + inFile.getName());
        errorHandling(ret);
        return ret;
    }

    private InvokeReturn internalDeriveFunctionsWithTM(File inFile, File libFile, File logFile, File stgOutFile, File eqnOutFile) {
        //@formatter:off
        List<String> params = Arrays.asList(
            "-o", stgOutFile.getName(),
            "-eqn", eqnOutFile.getName(),
            "-tm", "-dead", 
            "-log", logFile.getName(), 
            "-lib", libFile.getName(),
            inFile.getName()
        );
        //@formatter:on

        addInputFilesToCopy(inFile, libFile);
        addOutputFilesToExport(stgOutFile, eqnOutFile, logFile);

        InvokeReturn ret = run(params, "petrifyeqn_" + inFile.getName());
        errorHandling(ret);
        return ret;
    }

    private InvokeReturn internalProcessSTG(File stgInFile, File logFile, File stgOutFile) {
        //@formatter:off
        List<String> params = Arrays.asList(
            "-o", stgOutFile.getName(),
            "-dead", 
            "-log", logFile.getName(), 
            stgInFile.getName()
        );
        //@formatter:on

        addInputFilesToCopy(stgInFile);
        addOutputFilesToExport(stgOutFile, logFile);

        InvokeReturn ret = run(params, "petrify_" + stgInFile.getName());
        errorHandling(ret);
        return ret;
    }
}
