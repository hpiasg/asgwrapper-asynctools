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
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.asynctoolswrapper.model.MpsatTransitionSequences;
import de.uni_potsdam.hpi.asg.common.invoker.ExternalToolsInvoker;
import de.uni_potsdam.hpi.asg.common.invoker.InvokeReturn;
import de.uni_potsdam.hpi.asg.common.iohelper.FileHelper;
import de.uni_potsdam.hpi.asg.common.stg.GFile;
import de.uni_potsdam.hpi.asg.common.stg.model.Transition;
import de.uni_potsdam.hpi.asg.common.stg.model.Transition.Edge;

public class MpsatInvoker extends ExternalToolsInvoker {
    private static final Logger logger = LogManager.getLogger();

    private MpsatInvoker() {
        super("mpsat");
    }

    public static InvokeReturn getTraces(File pnmlFile, Transition transition) {
        return new MpsatInvoker().internalGetTraces(pnmlFile, transition);
    }

    public static InvokeReturn getTraces(File pnmlFile, String signalName, Edge edge) {
        return new MpsatInvoker().internalGetTraces(pnmlFile, signalName, edge);
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
        errorHandling(ret, Arrays.asList(0, 1));
        return ret;
    }

    private InvokeReturn internalGetTraces(File pnmlFile, String signalName, Edge edge) {
        String edgeStr = null;
        switch(edge) {
            case falling:
                edgeStr = "is_minus";
                break;
            case rising:
                edgeStr = "is_plus";
                break;
        }
        File reachFile = FileHelper.getInstance().newTmpFile("property.re");
        String reachStr = "exists t in tran S\"" + signalName + "\" { @t & " + edgeStr + " t }";
        if(!FileHelper.getInstance().writeFile(reachFile, reachStr)) {
            logger.error("Could not create reach file");
            return null;
        }
        String outFileName = "out.log";

        //@formatter:off
        List<String> params = Arrays.asList(
            "-Fs", "-d", "@" + reachFile.getName(), "-a", "-v1",
            pnmlFile.getName(),
            outFileName
        );
        //@formatter:on

        addInputFilesToCopy(pnmlFile, reachFile);
        addOutputFilesDownloadOnlyStartsWith(outFileName);

        InvokeReturn ret = run(params, "mpsat");
        if(!errorHandling(ret, Arrays.asList(0, 1))) {
            return ret;
        }

        File outFile = new File(localWorkingDir, outFileName);
        MpsatTransitionSequences seq = MpsatTransitionSequences.createFromOutFile(outFile);
        ret.setPayload(seq);

        return ret;
    }

    private InvokeReturn internalGetTraces(File pnmlFile, Transition transition) {
        File reachFile = FileHelper.getInstance().newTmpFile("property.re");
        String transitionStr = GFile.formatTransition(transition);
        String reachStr = "@T\"" + transitionStr + "\"";
        if(!FileHelper.getInstance().writeFile(reachFile, reachStr)) {
            logger.error("Could not create reach file");
            return null;
        }
        String outFileName = "out.log";

        //@formatter:off
        List<String> params = Arrays.asList(
            "-Fs", "-d", "@" + reachFile.getName(), "-a", "-v1",
            pnmlFile.getName(),
            outFileName
        );
        //@formatter:on

        addInputFilesToCopy(pnmlFile, reachFile);
        addOutputFilesDownloadOnlyStartsWith(outFileName);

        InvokeReturn ret = run(params, "mpsat");
        if(!errorHandling(ret, Arrays.asList(0, 1))) {
            return ret;
        }

        File outFile = new File(localWorkingDir, outFileName);
        MpsatTransitionSequences seq = MpsatTransitionSequences.createFromOutFile(outFile);
        ret.setPayload(seq);

        return ret;
    }
}
