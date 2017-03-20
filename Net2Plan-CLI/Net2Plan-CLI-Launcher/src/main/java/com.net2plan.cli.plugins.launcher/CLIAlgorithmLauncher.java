package com.net2plan.cli.plugins.launcher;

import com.net2plan.cli.plugins.CLINetworkDesign;

/**
 * Launcher using the CLI version of Net2Plan that helps the debugging of algorithms.
 * This main class receives the same input parameters as the CLINetworkDesign tool.
 * Set the parameter: '--class-file' to 'internal-algorithm' in order to search for the algorithm in the application class-path.
 */
public class CLIAlgorithmLauncher
{
    public static void main(String[] args)
    {
        try
        {
            CLINetworkDesign networkDesign = new CLINetworkDesign();
            networkDesign.executeFromCommandLine(args);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
