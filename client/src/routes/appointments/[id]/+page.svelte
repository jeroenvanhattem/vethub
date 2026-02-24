<script lang="ts">
	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import {
		getAppointmentById,
		updateAppointment,
		cancelAppointment,
		completeAppointment,
		deleteAppointment
	} from '$lib/api/appointment/AppointmentController';
	import type {
		AppointmentResponse,
		UpdateAppointmentRequest,
		AppointmentStatus
	} from '$lib/api/appointment/types';
	import { Button } from '$lib/components/ui/button';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { Textarea } from '$lib/components/ui/textarea';
	import { Badge } from '$lib/components/ui/badge';
	import { Calendar, ArrowLeft, Save, Ban, CheckCircle, Trash2, PawPrint, User } from 'lucide-svelte';
	import { toast } from 'svelte-sonner';

	const appointmentId = $derived(parseInt($page.params.id));

	let appointment = $state<AppointmentResponse | null>(null);
	let loading = $state(true);
	let saving = $state(false);
	let isEditing = $state(false);

	// Form state
	let scheduledDateTime = $state('');
	let reason = $state('');
	let status = $state<AppointmentStatus>('SCHEDULED');



	async function loadAppointment() {
		loading = true;
		try {
			appointment = await getAppointmentById(appointmentId);
			// Initialize form values
			scheduledDateTime = appointment.scheduledDateTime.slice(0, 16); // Format for datetime-local
			reason = appointment.reason;
			status = appointment.status;
		} catch (err) {
			toast.error('Failed to load appointment');
			console.error('Error loading appointment:', err);
		} finally {
			loading = false;
		}
	}

	async function handleSave() {
		if (!scheduledDateTime || !reason.trim()) {
			toast.error('Please fill in all required fields');
			return;
		}

		saving = true;
		try {
			const request: UpdateAppointmentRequest = {
				scheduledDateTime,
				reason: reason.trim(),
				status
			};

			appointment = await updateAppointment(appointmentId, request);
			toast.success('Appointment updated successfully');
			isEditing = false;
		} catch (err) {
			toast.error('Failed to update appointment');
			console.error('Error updating appointment:', err);
		} finally {
			saving = false;
		}
	}

	async function handleCancel() {
		try {
			appointment = await cancelAppointment(appointmentId);
			status = 'CANCELLED';
			toast.success('Appointment cancelled');
		} catch (err) {
			toast.error('Failed to cancel appointment');
			console.error('Error cancelling appointment:', err);
		}
	}

	async function handleComplete() {
		try {
			appointment = await completeAppointment(appointmentId);
			status = 'COMPLETED';
			toast.success('Appointment marked as completed');
		} catch (err) {
			toast.error('Failed to complete appointment');
			console.error('Error completing appointment:', err);
		}
	}

	async function handleDelete() {
		if (!confirm('Are you sure you want to delete this appointment? This action cannot be undone.')) {
			return;
		}
		
		try {
			await deleteAppointment(appointmentId);
			toast.success('Appointment deleted');
			goto('/appointments');
		} catch (err) {
			toast.error('Failed to delete appointment');
			console.error('Error deleting appointment:', err);
		}
	}

	function formatDateTime(dateTimeString: string): string {
		const date = new Date(dateTimeString);
		return date.toLocaleString('en-US', {
			year: 'numeric',
			month: 'long',
			day: 'numeric',
			hour: '2-digit',
			minute: '2-digit'
		});
	}

	function getStatusVariant(
		status: AppointmentStatus
	): 'default' | 'secondary' | 'destructive' | 'outline' {
		switch (status) {
			case 'SCHEDULED':
				return 'secondary';
			case 'CONFIRMED':
				return 'default';
			case 'CANCELLED':
				return 'destructive';
			case 'COMPLETED':
				return 'outline';
			default:
				return 'secondary';
		}
	}

	function getMinDateTime(): string {
		const now = new Date();
		const year = now.getFullYear();
		const month = String(now.getMonth() + 1).padStart(2, '0');
		const day = String(now.getDate()).padStart(2, '0');
		const hours = String(now.getHours()).padStart(2, '0');
		const minutes = String(now.getMinutes()).padStart(2, '0');
		return `${year}-${month}-${day}T${hours}:${minutes}`;
	}

	$effect(() => {
		loadAppointment();
	});
</script>

<svelte:head>
	<title>Appointment #{appointmentId} | VetHub</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<!-- Header -->
	<div class="mb-8 flex items-center justify-between">
		<div class="flex items-center gap-3">
			<div class="flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
				<Calendar class="h-6 w-6 text-primary" />
			</div>
			<div>
				<h1 class="text-2xl font-bold text-foreground">Appointment #{appointmentId}</h1>
				<p class="text-sm text-muted-foreground">View and manage appointment details</p>
			</div>
		</div>
		<Button variant="outline" href="/appointments">
			<ArrowLeft class="mr-2 h-4 w-4" />
			Back
		</Button>
	</div>

	{#if loading}
		<div class="card p-12 text-center">
			<div class="mx-auto mb-4 h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
			<p class="text-muted-foreground">Loading appointment...</p>
		</div>
	{:else if appointment}
		<div class="grid gap-6 md:grid-cols-3">
			<!-- Main Content -->
			<div class="md:col-span-2 space-y-6">
				{#if isEditing}
					<!-- Edit Form -->
					<div class="card p-6">
						<h2 class="text-lg font-semibold mb-4">Edit Appointment</h2>
						<div class="space-y-4">
							<div class="space-y-2">
								<Label for="datetime">Scheduled Date & Time *</Label>
								<Input
									id="datetime"
									type="datetime-local"
									bind:value={scheduledDateTime}
									min={getMinDateTime()}
									required
								/>
							</div>

							<div class="space-y-2">
								<Label for="reason">Reason for Visit *</Label>
								<Textarea id="reason" bind:value={reason} required rows={4} />
							</div>

							<div class="space-y-2">
								<Label for="status">Status *</Label>
								<select
									id="status"
									bind:value={status}
									required
									class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
								>
									<option value="SCHEDULED">Scheduled</option>
									<option value="CONFIRMED">Confirmed</option>
									<option value="CANCELLED">Cancelled</option>
									<option value="COMPLETED">Completed</option>
								</select>
							</div>

							<div class="flex gap-3">
								<Button onclick={handleSave} disabled={saving}>
									{#if saving}
										<div class="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-primary-foreground border-t-transparent"></div>
										Saving...
									{:else}
										<Save class="mr-2 h-4 w-4" />
										Save Changes
									{/if}
								</Button>
								<Button variant="outline" onclick={() => (isEditing = false)}>
									Cancel
								</Button>
							</div>
						</div>
					</div>
				{:else}
					<!-- View Mode -->
					<div class="card p-6 space-y-4">
						<div class="flex items-center justify-between">
							<h2 class="text-lg font-semibold">Appointment Details</h2>
							<Badge variant={getStatusVariant(appointment.status)}>
								{appointment.status}
							</Badge>
						</div>

						<div class="space-y-3">
							<div>
								<p class="text-sm text-muted-foreground">Scheduled Date & Time</p>
								<div class="flex items-center gap-2 mt-1">
									<Calendar class="h-4 w-4 text-primary" />
									<p class="font-medium">{formatDateTime(appointment.scheduledDateTime)}</p>
								</div>
							</div>

							<div>
								<p class="text-sm text-muted-foreground">Reason for Visit</p>
								<p class="mt-1">{appointment.reason}</p>
							</div>
						</div>

						<div class="pt-4 border-t flex gap-2">
							<Button onclick={() => (isEditing = true)}>Edit</Button>
							{#if appointment.status !== 'CANCELLED'}
								<Button variant="outline" onclick={handleCancel}>
									<Ban class="mr-2 h-4 w-4" />
									Cancel Appointment
								</Button>
							{/if}
							{#if appointment.status !== 'COMPLETED' && appointment.status !== 'CANCELLED'}
								<Button variant="outline" onclick={handleComplete}>
									<CheckCircle class="mr-2 h-4 w-4" />
									Mark Complete
								</Button>
							{/if}
						</div>
					</div>
				{/if}
			</div>

			<!-- Sidebar -->
			<div class="space-y-6">
				<!-- Pet Info -->
				<div class="card p-6">
					<h3 class="text-sm font-semibold mb-3">Pet Information</h3>
					<div class="flex items-center gap-3">
						<div class="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
							<PawPrint class="h-5 w-5 text-primary" />
						</div>
						<div>
							<p class="font-medium">{appointment.petName}</p>
							<p class="text-xs text-muted-foreground">Pet ID: {appointment.petId}</p>
						</div>
					</div>
				</div>

				<!-- Vet Info -->
				<div class="card p-6">
					<h3 class="text-sm font-semibold mb-3">Veterinarian</h3>
					<div class="flex items-center gap-3">
						<div class="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
							<User class="h-5 w-5 text-primary" />
						</div>
						<div>
							<p class="font-medium">{appointment.vetFirstName} {appointment.vetLastName}</p>
							<p class="text-xs text-muted-foreground">Vet ID: {appointment.vetId}</p>
						</div>
					</div>
				</div>

				<!-- Danger Zone -->
				<div class="card p-6 border-destructive/50">
					<h3 class="text-sm font-semibold text-destructive mb-3">Danger Zone</h3>
					<p class="text-xs text-muted-foreground mb-3">
						Once deleted, this appointment cannot be recovered.
					</p>
					<Button variant="destructive" size="sm" onclick={handleDelete}>
						<Trash2 class="mr-2 h-4 w-4" />
						Delete Appointment
					</Button>
				</div>
			</div>
		</div>
	{/if}
</div>
