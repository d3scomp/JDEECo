results-demo-X-Y-Z.csv      - Application-specific indicators for configurations of scenario Z 
                              at scale X with Y indicating whether boundary was enabled or disabled
                              ('f' = disabled, 't' = enabled).
                              One row per each pair Leader-Member
                              Columns: 
                              - end-to-end response time at the level of jDEECo process 
                              - end-to-end response time at the network level
                              - number of hops from Member to Leader
                              - version difference between Member getting into danger and 
                                Leader realyzing it (being notified ab out it)

results-generic-X-Y-Z.csv   - Generic indicators for configurations of scenario Z  
                              at scale X with Y indicating whether boundary was enabled or disabled
                              ('f' = disabled, 't' = enabled).
                              One row per each simulation run.
                              Columns:
                              - Total disseminated replicas
                              - Replicas succesfully received
                              - Number of Members in danger that should be discovered
                              - Number of Members in danger that were actually discovered
                              - Number of disseminations prevented by boundary condition

results-neighbors-X-Y-Z.csv - Neighbor counts for for configurations of scenario Z  
                              at scale X with Y indicating whether boundary was enabled or disabled.
                              One row per each component.
                              Columns:
                              - Number of MANET neighbors of the component


analysis/                   - Detailed analysis results for individual simulation runs   