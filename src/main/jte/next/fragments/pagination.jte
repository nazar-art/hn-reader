@param int currentPage
@param int totalPages

!{var isBeginning = currentPage <= 1;}
!{var isEnding = currentPage >= totalPages;}

<nav aria-label="Pagination">
    <ul>
        <li>
            <a href="/?page=1"
               class="${isBeginning ? "disabled" : ""}"
               aria-disabled="${isBeginning}">
                First
            </a>
        </li>
        <li>
            <a href="/?page=${currentPage - 1}"
               class="${isBeginning ? "disabled" : ""}"
               aria-disabled="${isBeginning}">
                Previous
            </a>
        </li>

        @if (currentPage > 2)
            <li>
                <a href="/?page=${currentPage - 2}">
                    ${currentPage - 2}
                </a>
            </li>
        @endif

        @if (currentPage > 1)
            <li>
                <a href="/?page=${currentPage - 1}">
                    ${currentPage - 1}
                </a>
            </li>
        @endif

        <li>
            <a href="/?page=${currentPage}" aria-current="page">
                ${currentPage}
            </a>
        </li>

        @if (currentPage < totalPages)
            <li>
                <a href="/?page=${currentPage + 1}">
                    ${currentPage + 1}
                </a>
            </li>
        @endif

        @if (currentPage < totalPages - 1)
            <li>
                <a href="/?page=${currentPage + 2}">
                    ${currentPage + 2}
                </a>
            </li>
        @endif

        <li>
            <a href="/?page=${currentPage + 1}"
               class="${isEnding ? "disabled" : ""}"
               aria-disabled="${isEnding}">
                Next
            </a>
        </li>
        <li>
            <a href="/?page=${totalPages}"
               class="${isEnding ? "disabled" : ""}"
               aria-disabled="${isEnding}">
                Last
            </a>
        </li>
    </ul>
</nav>
