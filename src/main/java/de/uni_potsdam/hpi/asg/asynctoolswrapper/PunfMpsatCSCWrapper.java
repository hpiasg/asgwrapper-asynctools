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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.common.invoker.InvokeReturn;
import de.uni_potsdam.hpi.asg.common.iohelper.WorkingdirGenerator;

public class PunfMpsatCSCWrapper {
    private static final Logger logger = LogManager.getLogger();

    private PunfMpsatCSCWrapper() {
    }

    public static InvokeReturn solveCSC(File inFile, File outFile) {
        File workingDir = WorkingdirGenerator.getInstance().getWorkingDir();
        File mciFile = new File(workingDir, inFile.getName() + ".mci");

        InvokeReturn retPunft = PunfInvoker.unfoldGFile(inFile, mciFile);
        if(retPunft == null || !retPunft.getResult()) {
            logger.error("PUNF Error with " + inFile);
            return null;
        }

        InvokeReturn retMpsat = MpsatInvoker.solveCSC(mciFile, outFile);
        if(retMpsat == null || !retMpsat.getResult()) {
            logger.error("MPSAT Error with " + mciFile);
        }

        InvokeReturn retVal = new InvokeReturn(null);
        retVal.setResult(true);
        retVal.setLocalSystemTime(retPunft.getLocalSystemTime() + retMpsat.getLocalSystemTime());
        retVal.setLocalUserTime(retPunft.getLocalUserTime() + retMpsat.getLocalUserTime());
        retVal.setRemoteSystemTime(retPunft.getRemoteSystemTime() + retMpsat.getRemoteSystemTime());
        retVal.setRemoteUserTime(retPunft.getRemoteUserTime() + retMpsat.getRemoteUserTime());
        return retVal;
    }
}
