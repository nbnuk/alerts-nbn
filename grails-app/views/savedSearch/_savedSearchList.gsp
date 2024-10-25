<g:each in="${savedSearches}" status="i" var="search">
    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" id='search-${search.id}'>
        <td>${search.name}</td>
        <td>${search.description}</td>
        <td class="searchActions">
            <button class="btn btn-primary btn-sm editSearch" data-id="${search.id}">Edit</button>
            <button class="btn btn-danger btn-sm deleteSearch" data-id="${search.id}">Delete</button>
        </td>
    </tr>
</g:each>
