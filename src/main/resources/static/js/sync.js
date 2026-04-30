const form = document.querySelector('form[data-note-id]');
const statusEl = document.getElementById('sync-status');

if (form) {
    const noteId = form.dataset.noteId;
    let timer;

    const setStatus = (msg) => { if (statusEl) statusEl.textContent = msg; };

    const sync = () => {
        const title = form.querySelector('[name="title"]').value;
        const content = form.querySelector('[name="content"]').value;

        fetch(`/notes/${noteId}/sync`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, content })
        })
        .then(res => { setStatus(res.ok ? 'Saved' : 'Sync failed'); })
        .catch(() => { setStatus('Sync failed'); });
    };

    form.addEventListener('input', () => {
        setStatus('Saving...');
        clearTimeout(timer);
        timer = setTimeout(sync, 1000);
    });
}
