<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
    <!-- Sidebar - Brand -->
    <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index">
        <div class="sidebar-brand-icon rotate-n-15">
            <i class="fas fa-confluence"></i>
        </div>
        <div class="sidebar-brand-text mx-3">Kafka Tool</div>
    </a>

    <!-- Divider -->
    <hr class="sidebar-divider my-0">

    <!-- Nav Item - Dashboard -->
    <li class="nav-item">
        <a id="index" class="nav-link" href="/index">
            <i class="fas fa-fw fa-tachometer-alt"></i>
            <span>Dashboard</span>
        </a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item">
        <a class="nav-link" href="#" data-toggle="collapse" data-target="#operation" aria-expanded="true" aria-controls="operation">
            <i class="fas fa-fw fa-tasks"></i>
            <span>Operation</span>
        </a>
        <div id="operation" class="collapse show" aria-labelledby="headingOperation" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <a id="consume" class="collapse-item" href="/consume"><i class="fas fa-fw fa-download"></i> Consume</a>
                <a id="produce" class="collapse-item" href="/produce"><i class="fas fa-fw fa-upload"></i> Produce</a>
            </div>
        </div>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider d-none d-md-block">

    <li class="nav-item">
        <a class="nav-link" href="#" data-toggle="collapse" data-target="#management" aria-expanded="true" aria-controls="management">
            <i class="fas fa-fw fa-desktop"></i>
            <span>Management</span>
        </a>
        <div id="monitoring" class="collapse show" aria-labelledby="headingOperation" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <a id="consumer-group" class="collapse-item" href="/consumer-group"><i class="fas fa-fw fa-eye"></i> Consumer Group</a>
                <a id="topic" class="collapse-item" href="/topic"><i class="fas fa-fw fa-comment"></i> Topic</a>
            </div>
        </div>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider d-none d-md-block">

    <!-- Sidebar Toggler (Sidebar) -->
    <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
    </div>

    <div class="author text-center">

        <div>Dieu Nguyen</div>
        <div><i>hoangdieuctu@gmail.com</i></div>
        <div style="padding-top: 10px">
            <span title="Like" id="like-icon" class="fa fa-heart"></span> <span id="like-count">0</span>
        </div>
    </div>
</ul>