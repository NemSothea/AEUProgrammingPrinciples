package com.example.demo.threads;

import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareMonitor {
    private static final AtomicBoolean monitoring = new AtomicBoolean(false);
    private static ScheduledExecutorService scheduler;
    private static List<Future<?>> monitoringTasks;

    public static void main(String[] args) {
        System.out.println("=== Java Hardware Monitoring with Threads ===");
        System.out.println("Starting at: " + new Date());

        Scanner scanner = new Scanner(System.in);
        monitoringTasks = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(5);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));

        while (true) {
            printMenu();
            int choice = getIntInput(scanner, "Choose an option (1-8): ");

            switch (choice) {
                case 1:
                    startCPUMonitoring();
                    break;
                case 2:
                    startMemoryMonitoring();
                    break;
                case 3:
                    startDiskMonitoring();
                    break;
                case 4:
                    startNetworkMonitoring();
                    break;
                case 5:
                    startSystemInfoMonitoring();
                    break;
                case 6:
                    startComprehensiveMonitoring();
                    break;
                case 7:
                    stopAllMonitoring();
                    break;
                case 8:
                    shutdown();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }

            waitForEnter(scanner);
        }
    }

    private static void printMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("HARDWARE MONITORING MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Start CPU Monitoring");
        System.out.println("2. Start Memory Monitoring");
        System.out.println("3. Start Disk Monitoring");
        System.out.println("4. Start Network Monitoring");
        System.out.println("5. Start System Info Monitoring");
        System.out.println("6. Start Comprehensive Monitoring (All)");
        System.out.println("7. Stop All Monitoring");
        System.out.println("8. Exit");
        System.out.println("=".repeat(60));
    }

    private static int getIntInput(Scanner scanner, String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // CPU Monitoring
    private static void startCPUMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting CPU Monitoring ---");
        monitoring.set(true);

        Future<?> cpuTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorCPU();
            } catch (Exception e) {
                System.err.println("Error in CPU monitoring: " + e.getMessage());
            }
        }, 0, 2, TimeUnit.SECONDS);

        monitoringTasks.add(cpuTask);
        System.out.println("CPU monitoring started! Updating every 2 seconds.");
    }

    private static void monitorCPU() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        // Get CPU usage
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            double systemCpuLoad = sunOsBean.getSystemCpuLoad() * 100;
            double processCpuLoad = sunOsBean.getProcessCpuLoad() * 100;
            long availableProcessors = sunOsBean.getAvailableProcessors();

            System.out.println("\n" + "=".repeat(40));
            System.out.println("CPU STATISTICS - " + new Date());
            System.out.println("=".repeat(40));
            System.out.printf("System CPU Usage: %.2f%%\n", systemCpuLoad);
            System.out.printf("Process CPU Usage: %.2f%%\n", processCpuLoad);
            System.out.printf("Available Processors: %d\n", availableProcessors);
            System.out.printf("System Load Average: %.2f\n", osBean.getSystemLoadAverage());
        }

        // Thread information
        System.out.printf("Live Threads: %d\n", threadBean.getThreadCount());
        System.out.printf("Peak Threads: %d\n", threadBean.getPeakThreadCount());
        System.out.printf("Total Started Threads: %d\n", threadBean.getTotalStartedThreadCount());

        // Monitor top CPU threads
        monitorTopThreads(threadBean);
    }

    private static void monitorTopThreads(ThreadMXBean threadBean) {
        long[] threadIds = threadBean.getAllThreadIds();
        Map<Long, Long> threadCPU = new HashMap<>();

        for (long threadId : threadIds) {
            long cpuTime = threadBean.getThreadCpuTime(threadId);
            if (cpuTime != -1) {
                threadCPU.put(threadId, cpuTime);
            }
        }

        // Sort by CPU time
        System.out.println("Top 5 CPU-consuming threads:");
        threadCPU.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    long threadId = entry.getKey();
                    ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
                    if (threadInfo != null) {
                        double cpuTimeMs = entry.getValue() / 1_000_000.0;
                        System.out.printf("  %-25s CPU: %8.2fms\n",
                                truncateThreadName(threadInfo.getThreadName()), cpuTimeMs);
                    }
                });
    }

    private static String truncateThreadName(String name) {
        return name.length() > 25 ? name.substring(0, 22) + "..." : name;
    }

    // Memory Monitoring
    private static void startMemoryMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting Memory Monitoring ---");
        monitoring.set(true);

        Future<?> memoryTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorMemory();
            } catch (Exception e) {
                System.err.println("Error in memory monitoring: " + e.getMessage());
            }
        }, 0, 3, TimeUnit.SECONDS);

        monitoringTasks.add(memoryTask);
        System.out.println("Memory monitoring started! Updating every 3 seconds.");
    }

    private static void monitorMemory() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();

        System.out.println("\n" + "=".repeat(40));
        System.out.println("MEMORY STATISTICS - " + new Date());
        System.out.println("=".repeat(40));

        // Heap Memory
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        double heapUsagePercent = (heapUsage.getUsed() * 100.0 / heapUsage.getMax());
        System.out.printf("Heap Memory:     %8s / %8s (%5.1f%%)\n",
                formatBytes(heapUsage.getUsed()),
                formatBytes(heapUsage.getMax()),
                heapUsagePercent);

        // Non-Heap Memory
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
        System.out.printf("Non-Heap Memory: %8s / %8s\n",
                formatBytes(nonHeapUsage.getUsed()),
                formatBytes(nonHeapUsage.getMax()));

        // System Memory
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            long totalPhysical = sunOsBean.getTotalPhysicalMemorySize();
            long freePhysical = sunOsBean.getFreePhysicalMemorySize();
            long usedPhysical = totalPhysical - freePhysical;
            double physicalUsagePercent = (usedPhysical * 100.0 / totalPhysical);

            System.out.printf("Physical Memory: %8s / %8s (%5.1f%%)\n",
                    formatBytes(usedPhysical),
                    formatBytes(totalPhysical),
                    physicalUsagePercent);

            long totalSwap = sunOsBean.getTotalSwapSpaceSize();
            long freeSwap = sunOsBean.getFreeSwapSpaceSize();
            long usedSwap = totalSwap - freeSwap;
            double swapUsagePercent = totalSwap > 0 ? (usedSwap * 100.0 / totalSwap) : 0;

            System.out.printf("Swap Space:      %8s / %8s (%5.1f%%)\n",
                    formatBytes(usedSwap),
                    formatBytes(totalSwap),
                    swapUsagePercent);
        }

        // Memory Pools
        System.out.println("\nMemory Pools:");
        for (MemoryPoolMXBean pool : memoryPools) {
            if (pool.getUsage().getMax() > 0) {
                MemoryUsage usage = pool.getUsage();
                double usagePercent = (usage.getUsed() * 100.0 / usage.getMax());
                System.out.printf("  %-20s: %8s / %8s (%5.1f%%)\n",
                        truncatePoolName(pool.getName()),
                        formatBytes(usage.getUsed()),
                        formatBytes(usage.getMax()),
                        usagePercent);
            }
        }

        // Run garbage collection if memory is high
        if (heapUsagePercent > 80) {
            System.out.println("⚠️  High memory usage! Running garbage collection...");
            memoryBean.gc();
        }
    }

    private static String truncatePoolName(String name) {
        return name.length() > 20 ? name.substring(0, 17) + "..." : name;
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // Disk Monitoring
    private static void startDiskMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting Disk Monitoring ---");
        monitoring.set(true);

        Future<?> diskTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorDisk();
            } catch (Exception e) {
                System.err.println("Error in disk monitoring: " + e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS);

        monitoringTasks.add(diskTask);
        System.out.println("Disk monitoring started! Updating every 5 seconds.");
    }

    private static void monitorDisk() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("DISK STATISTICS - " + new Date());
        System.out.println("=".repeat(40));

        File[] roots = File.listRoots();
        boolean lowSpaceWarning = false;

        for (File root : roots) {
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            long used = total - free;
            double usagePercent = (used * 100.0 / total);

            String driveName = root.getAbsolutePath();
            if (driveName.endsWith("\\")) {
                driveName = driveName.substring(0, driveName.length() - 1);
            }

            System.out.printf("Drive %s:\n", driveName);
            System.out.printf("  Total: %8s, Free: %8s, Used: %8s (%5.1f%%)\n",
                    formatBytes(total),
                    formatBytes(free),
                    formatBytes(used),
                    usagePercent);

            // Warn if disk space is low
            if (usagePercent > 90) {
                System.out.println("  ⚠️  LOW DISK SPACE!");
                lowSpaceWarning = true;
            }
        }

        if (lowSpaceWarning) {
            System.out.println("\n💡 Recommendation: Clean up disk space or expand storage");
        }

        // Monitor disk I/O (simplified)
        try {
            monitorDiskIO();
        } catch (Exception e) {
            // Disk I/O monitoring might not be available on all systems
        }
    }

    private static void monitorDiskIO() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            try {
                long committedVirtualMemory = sunOsBean.getCommittedVirtualMemorySize();
                System.out.printf("Committed Virtual Memory: %s\n",
                        formatBytes(committedVirtualMemory));
            } catch (Exception e) {
                // Method not available on this platform
            }
        }
    }

    // Network Monitoring
    private static void startNetworkMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting Network Monitoring ---");
        monitoring.set(true);

        Future<?> networkTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorNetwork();
            } catch (Exception e) {
                System.err.println("Error in network monitoring: " + e.getMessage());
            }
        }, 0, 4, TimeUnit.SECONDS);

        monitoringTasks.add(networkTask);
        System.out.println("Network monitoring started! Updating every 4 seconds.");
    }

    private static void monitorNetwork() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("NETWORK STATISTICS - " + new Date());
        System.out.println("=".repeat(40));

        // Network interfaces
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            int interfaceCount = 0;

            while (interfaces.hasMoreElements()) {
                NetworkInterface netInt = interfaces.nextElement();
                if (netInt.isUp() && !netInt.isLoopback()) {
                    interfaceCount++;
                    System.out.printf("Interface %d: %s (%s)\n",
                            interfaceCount, netInt.getDisplayName(), netInt.getName());

                    // Display IP addresses
                    Enumeration<InetAddress> addresses = netInt.getInetAddresses();
                    List<String> ipList = new ArrayList<>();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (!addr.isLoopbackAddress()) {
                            ipList.add(addr.getHostAddress());
                        }
                    }

                    if (!ipList.isEmpty()) {
                        System.out.printf("  IP Addresses: %s\n", String.join(", ", ipList));
                    }

                    System.out.printf("  MTU: %d, Virtual: %s, Multicast: %s\n",
                            netInt.getMTU(), netInt.isVirtual(), netInt.supportsMulticast());

                    System.out.printf("  Status: %s, Point-to-Point: %s\n",
                            netInt.isUp() ? "UP" : "DOWN",
                            netInt.isPointToPoint());

                    System.out.println();
                }
            }

            if (interfaceCount == 0) {
                System.out.println("No active network interfaces found.");
            }

        } catch (SocketException e) {
            System.err.println("Error reading network interfaces: " + e.getMessage());
        }
    }

    // System Information Monitoring
    private static void startSystemInfoMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting System Information Monitoring ---");
        monitoring.set(true);

        Future<?> systemTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorSystemInfo();
            } catch (Exception e) {
                System.err.println("Error in system info monitoring: " + e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);

        monitoringTasks.add(systemTask);
        System.out.println("System info monitoring started! Updating every 10 seconds.");
    }

    private static void monitorSystemInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        System.out.println("\n" + "=".repeat(40));
        System.out.println("SYSTEM INFORMATION - " + new Date());
        System.out.println("=".repeat(40));

        // Basic system info
        // Fixed code
        System.out.printf("Operating System: %s %s\n", osBean.getName(), osBean.getVersion());
        System.out.printf("Architecture: %s\n", System.getProperty("os.arch"));

        // JVM info
        System.out.printf("JVM: %s (%s)\n", runtimeBean.getVmName(), runtimeBean.getVmVersion());
        System.out.printf("JVM Vendor: %s\n", runtimeBean.getVmVendor());
        System.out.printf("JVM Uptime: %s\n", formatUptime(runtimeBean.getUptime()));
        System.out.printf("JVM Start Time: %s\n", new Date(runtimeBean.getStartTime()));

        // Class loading info
        ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
        System.out.printf("Loaded Classes: %d, Total Loaded: %d, Unloaded: %d\n",
                classBean.getLoadedClassCount(),
                classBean.getTotalLoadedClassCount(),
                classBean.getUnloadedClassCount());

        // Garbage collection info
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        System.out.println("\nGarbage Collectors:");
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("  %s: Collections=%d, Time=%dms\n",
                    gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());
        }

        // System properties (key ones only)
        System.out.println("\nKey System Properties:");
        Properties props = System.getProperties();
        String[] keyProps = { "java.version", "java.vendor", "java.home",
                "user.name", "user.dir", "os.name", "os.arch", "os.version" };

        for (String key : keyProps) {
            String value = props.getProperty(key);
            if (value != null) {
                System.out.printf("  %s: %s\n", key, value);
            }
        }
    }

    private static String formatUptime(long uptime) {
        long seconds = uptime / 1000;
        long days = seconds / (24 * 3600);
        seconds %= (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        if (days > 0) {
            return String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    // Comprehensive Monitoring
    private static void startComprehensiveMonitoring() {
        if (monitoring.get()) {
            System.out.println("Monitoring already running. Stop first.");
            return;
        }

        System.out.println("\n--- Starting Comprehensive Hardware Monitoring ---");
        monitoring.set(true);

        // Clear any existing tasks
        monitoringTasks.clear();

        // Start all monitoring tasks with different intervals
        Future<?> cpuTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorCPU();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        Future<?> memoryTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 3, TimeUnit.SECONDS);

        Future<?> diskTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorDisk();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2, 5, TimeUnit.SECONDS);

        Future<?> networkTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorNetwork();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3, 4, TimeUnit.SECONDS);

        Future<?> systemTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                monitorSystemInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 4, 10, TimeUnit.SECONDS);

        monitoringTasks.add(cpuTask);
        monitoringTasks.add(memoryTask);
        monitoringTasks.add(diskTask);
        monitoringTasks.add(networkTask);
        monitoringTasks.add(systemTask);

        System.out.println("\n✅ All hardware monitoring started!");
        System.out.println("Monitoring Intervals:");
        System.out.println("  CPU: Every 2 seconds");
        System.out.println("  Memory: Every 3 seconds");
        System.out.println("  Disk: Every 5 seconds");
        System.out.println("  Network: Every 4 seconds");
        System.out.println("  System Info: Every 10 seconds");
        System.out.println("\nPress Enter to stop monitoring...");
    }

    private static void stopAllMonitoring() {
        if (!monitoring.get()) {
            System.out.println("No monitoring is currently running.");
            return;
        }

        System.out.println("\n--- Stopping All Monitoring ---");
        monitoring.set(false);

        int stoppedCount = 0;
        for (Future<?> task : monitoringTasks) {
            if (!task.isDone() && !task.isCancelled()) {
                task.cancel(true);
                stoppedCount++;
            }
        }

        monitoringTasks.clear();
        System.out.println("Stopped " + stoppedCount + " monitoring tasks.");
        System.out.println("All hardware monitoring has been stopped.");
    }

    private static void shutdown() {
        System.out.println("\nShutting down Hardware Monitor...");
        stopAllMonitoring();

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                    System.out.println("Forcing shutdown...");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Hardware Monitor shutdown complete.");
        System.out.println("Goodbye!");
    }
}
