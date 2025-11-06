function initializeTooltips() {
    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        tooltipTriggerList.forEach(el => new bootstrap.Tooltip(el));
    }
}

function loadFallbackScript(src, callback) {
    const script = document.createElement('script');
    script.src = src;
    script.defer = true;
    if (callback) script.onload = callback;
    document.head.appendChild(script);
}

function loadFallbackStylesheet(href) {
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = href;
    document.head.appendChild(link);
}

// Check Bootstrap CSS
if (!window.getComputedStyle(document.documentElement).getPropertyValue('--bs-body-font-size')) {
    loadFallbackStylesheet('/css/bootstrap.min.css');
}

// Check FontAwesome CSS
if (!document.querySelector('i.fa')) {
    loadFallbackStylesheet('/css/fontawesome.all.min.css');
}

// Check Bootstrap JS
if (typeof bootstrap === 'undefined') {
    loadFallbackScript('/js/bootstrap.bundle.min.js', initializeTooltips);
} else {
    initializeTooltips();
}